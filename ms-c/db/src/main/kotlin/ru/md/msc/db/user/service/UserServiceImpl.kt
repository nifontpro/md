package ru.md.msc.db.user.service

import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.msc.db.dept.model.DeptDetailsEntity
import ru.md.msc.db.dept.model.DeptEntity
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.dept.service.DeptErrors
import ru.md.msc.db.user.model.UserDetailsEntity
import ru.md.msc.db.user.model.mappers.toUser
import ru.md.msc.db.user.model.mappers.toUserDept
import ru.md.msc.db.user.model.mappers.toUserDetails
import ru.md.msc.db.user.model.mappers.toUserDetailsEntity
import ru.md.msc.db.user.model.role.RoleEntity
import ru.md.msc.db.user.repo.RoleRepository
import ru.md.msc.db.user.repo.UserDetailsRepository
import ru.md.msc.db.user.repo.UserRepository
import ru.md.msc.domain.base.model.RepositoryData
import ru.md.msc.domain.dept.model.DeptType
import ru.md.msc.domain.user.model.RoleUser
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.domain.user.service.UserService
import java.time.LocalDateTime

// id корневого отдела программы
const val ROOT_DEPT_ID = 1L

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

		val userDetailsEntity = (userDetails.toUserDetailsEntity(create = true))

		val deptEntity = DeptEntity(
			parentId = ROOT_DEPT_ID,
			name = "Владелец " + userDetails.user.authEmail,
			classname = "Корневой",
			type = DeptType.USER_OWNER,
		)
		val deptDetailsEntity = DeptDetailsEntity(
			dept = deptEntity,
			address = userDetails.address,
			email = userDetails.user.authEmail,
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

		addRolesToUserEntity(userDetails, userDetailsEntity)

		return RepositoryData.success(data = userDetailsEntity.toUserDetails())
	}

	override fun create(userDetails: UserDetails): RepositoryData<UserDetails> {
		val userDetailsEntity = (userDetails.toUserDetailsEntity(create = true))
		try {
			userDetailsRepository.save(userDetailsEntity)
		} catch (e: Exception) {
			log.error(e.message)
			return UserErrors.create()
		}
		addRolesToUserEntity(userDetails, userDetailsEntity)
		return RepositoryData.success(data = userDetailsEntity.toUserDetails())
	}

	private fun addRolesToUserEntity(
		userDetails: UserDetails,
		userDetailsEntity: UserDetailsEntity
	) {
		userDetails.user.roles.map { roleEnum ->
			val roleEntity = RoleEntity(roleUser = roleEnum, user = userDetailsEntity.user)
			userDetailsEntity.user?.roles?.add(roleEntity)
		}
	}

	override fun doesOwnerWithEmailExist(email: String): RepositoryData<Boolean> {
		val roles = try {
			roleRepository.findByRoleUserAndUserAuthEmail(
				roleUser = RoleUser.OWNER,
				userEmail = email
			)
		} catch (e: Exception) {
			return UserErrors.getOwnerByEmailExist()
		}
		return RepositoryData.success(data = roles.isNotEmpty())
	}

	override fun findByAuthEmailWithDept(authEmail: String): RepositoryData<List<User>> {
		return try {
			val users = userRepository.findByAuthEmailIgnoreCase(authEmail = authEmail).map {
				it.toUserDept()
			}
			RepositoryData.success(data = users)
		} catch (e: Exception) {
			return UserErrors.getError()
		}
	}

	override fun findByDeptId(deptId: Long): RepositoryData<List<User>> {
		return try {
			val users = userRepository.findByDeptId(deptId = deptId).map {
				it.toUserDept()
			}
			RepositoryData.success(data = users)
		} catch (e: Exception) {
			return UserErrors.getError()
		}
	}

	override fun findById(userId: Long): RepositoryData<User> {
		return try {
			val user = userRepository.findByIdOrNull(userId)?.toUser() ?: return UserErrors.userNotFound()
			RepositoryData.success(data = user)
		} catch (e: Exception) {
			UserErrors.getError()
		}
	}

	override fun findByIdDetails(userId: Long): RepositoryData<UserDetails> {
		return try {
			val userDetails = userDetailsRepository.findByUserId(userId)?.toUserDetails() ?: return UserErrors.userNotFound()
			RepositoryData.success(data = userDetails)
		} catch (e: Exception) {
			UserErrors.getError()
		}
	}

	override fun deleteById(userId: Long): RepositoryData<UserDetails> {
		val userDetailsEntity = try {
			userDetailsRepository.findByIdOrNull(userId) ?: return UserErrors.userNotFound()
		} catch (e: Exception) {
			return UserErrors.getError()
		}

		return try {
			userRepository.deleteById(userDetailsEntity.user?.id ?: 0)
			RepositoryData.success(data = userDetailsEntity.toUserDetails())
		} catch (e: Exception) {
			UserErrors.deleteError()
		}

	}

	val log: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

}