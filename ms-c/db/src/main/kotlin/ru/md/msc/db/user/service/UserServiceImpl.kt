package ru.md.msc.db.user.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.md.msc.db.dept.model.DeptDetailsEntity
import ru.md.msc.db.dept.model.DeptEntity
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.user.model.mappers.toUser
import ru.md.msc.db.user.model.mappers.toUserDetailsEntity
import ru.md.msc.db.user.repo.RoleRepository
import ru.md.msc.db.user.repo.UserDetailsRepository
import ru.md.msc.db.user.repo.UserRepository
import ru.md.msc.domain.user.model.RoleEnum
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.domain.user.service.UserService
import ru.md.msc.db.user.model.role.RoleEntity
import java.time.LocalDateTime


@Service
@Transactional
class UserServiceImpl(
	private val userRepository: UserRepository,
	private val userDetailsRepository: UserDetailsRepository,
	private val roleRepository: RoleRepository,
	private val deptDetailsRepository: DeptDetailsRepository
) : UserService {

	override fun getAll(): List<User> {
//		return userRepository.findAll().map { it.toUser() }
		return userRepository.findAll().map { it.toUser() }
	}

	override fun add(userDetails: UserDetails) {
		val userDetailsEntity = userDetailsRepository.save(userDetails.toUserDetailsEntity())
		val user = userDetailsEntity.user
		val role = RoleEntity(code = RoleEnum.USER.code, userId = user.id ?: 0)
		roleRepository.save(role)
//		user.addRole(role)
	}

//	fun addRole(request: AddUserRoleRequest) {
//		val role = RoleEntity(code = request.role.code, userId = request.userId)
//		roleRepository.save(role)
//	}

	fun testDept() {
		val dept = DeptEntity(
			parentId = 21,
			name = "test 2",
//			createdAt = Timestamp(System.currentTimeMillis()),
		)

		val details = DeptDetailsEntity(
			address = "Penza",
			email = "testCompany@mail.ru",
			dept = dept
		)

		deptDetailsRepository.save(details)
	}
}