package ru.md.msc.db.user.service

import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.md.base.dom.model.RepositoryData
import ru.md.msc.db.dept.model.DeptDetailsEntity
import ru.md.msc.db.dept.model.DeptEntity
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.dept.service.DeptErrors
import ru.md.msc.db.user.model.mappers.toUser
import ru.md.msc.db.user.model.mappers.toUserDetails
import ru.md.msc.db.user.model.mappers.toUserDetailsEntity
import ru.md.msc.db.user.model.role.RoleEntity
import ru.md.msc.db.user.repo.RoleRepository
import ru.md.msc.db.user.repo.UserDetailsRepository
import ru.md.msc.db.user.repo.UserRepository
import ru.md.msc.domain.dept.model.DeptType
import ru.md.msc.domain.user.model.RoleEnum
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.domain.user.service.UserService
import java.time.LocalDateTime

@Service
@Transactional
class UserServiceImpl(
	private val userRepository: UserRepository,
	private val userDetailsRepository: UserDetailsRepository,
	private val roleRepository: RoleRepository,
	private val deptDetailsRepository: DeptDetailsRepository
) : UserService {

	/**
	 * Создание корневого владельца вместе с отделом
	 */
	override fun createOwner(userDetails: UserDetails): RepositoryData<UserDetails> {

		val userDetailsEntity = (userDetails.toUserDetailsEntity())

		val deptEntity = DeptEntity(
			parentId = 1, // Найти id Корневого отдела ROOT
			name = "Владелец " + userDetails.user?.email,
			classname = "Корневой",
			type = DeptType.USER,
		)
		val deptDetailsEntity = DeptDetailsEntity(
			dept = deptEntity,
			address = userDetails.address,
			email = userDetails.user?.email,
			phone = userDetails.phone,
			createdAt = LocalDateTime.now()
		)

		try {
			deptDetailsRepository.save(deptDetailsEntity)
		} catch (e: Exception) {
			log.error(e.message)
			return DeptErrors.createDept()
		}

		userDetailsEntity.user?.dept = deptEntity

		try {
			userDetailsRepository.save(userDetailsEntity)
		} catch (e: Exception) {
			log.error(e.message)
			return UserErrors.createOwner()
		}

		val roleOwner = RoleEntity(roleEnum = RoleEnum.OWNER, user = userDetailsEntity.user)
		val roleAdmin = RoleEntity(roleEnum = RoleEnum.ADMIN, user = userDetailsEntity.user)
		val roles = listOf(roleOwner, roleAdmin)
		userDetailsEntity.user?.roles?.addAll(roles)
		// Автоматическое добавление ролей при завершении транзакции

		return RepositoryData.success(data = userDetailsEntity.toUserDetails())
	}

	override fun doesOwnerWithEmailExist(email: String): RepositoryData<Boolean> {
		val roles = try {
			roleRepository.findByRoleEnumAndUserEmail(
				roleEnum = RoleEnum.OWNER,
				userEmail = email
			)
		} catch (e: Exception) {
			return UserErrors.getOwnerByEmailExist()
		}

		log.info("Проверка наличия Владельца по email: $roles")

		return RepositoryData.success(data = roles.isNotEmpty())
	}

	override fun getAll(): List<User> {
		return userRepository.findAll().map { it.toUser() }
	}

	override fun add(userDetails: UserDetails) {
		val userDetailsEntity = userDetailsRepository.save(userDetails.toUserDetailsEntity())
		val user = userDetailsEntity.user
		val role = RoleEntity(roleEnum = RoleEnum.USER, user = user)
		roleRepository.save(role)
//		user.addRole(role)
	}

	companion object {
		var log: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)
	}

}