package ru.md.msc.db.tc

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.base_domain.model.BaseOrder
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.Direction
import ru.md.msc.db.user.repo.RoleRepository
import ru.md.msc.db.user.repo.UserRepository
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.user.model.RoleUser
import ru.md.msc.domain.user.service.UserService

@SpringBootTest(classes = [TestBeans::class])
class UserTest(
	@Autowired private val deptService: DeptService,
	@Autowired private val userService: UserService,
	@Autowired private val roleRepository: RoleRepository,
	@Autowired private val userRepository: UserRepository
) {

	@Test
	fun deptTest() {
		val depts = deptService.findSubTreeDepts(1)
		println(depts)
	}

	@Test
	fun userTest() {
		val users = userService.findBySubDepts(
			deptId = 2,
			baseQuery = BaseQuery(orders = listOf(BaseOrder("id", Direction.ASC)))
		)
		println(users)
	}

	@Test
	fun ownerExistTest() {
		val count = roleRepository.countByUserIdAndRoleUser(
			userId = 1,
			roleUser = RoleUser.OWNER
		)
		assertEquals(1,count)
	}

	@Test
	fun ownerNotExistTest() {
		val count = roleRepository.countByUserIdAndRoleUser(
			userId = 2,
			roleUser = RoleUser.OWNER
		)
		assertEquals(0,count)
	}

	@Test
	@Transactional
	fun userToArchive() {
		val userId = 2L
		userRepository.moveUserToArchive(userId = userId)
		val user = userService.findById(userId = userId)
		assertEquals(true, user?.archive)
	}
}
