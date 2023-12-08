package ru.md.msc.db.tc.user

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.base_domain.model.BaseOrder
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.Direction
import ru.md.base_domain.user.model.RoleUser
import ru.md.base_domain.user.service.BaseUserService
import ru.md.msc.db.tc.TestBeans
import ru.md.msc.db.tc.ownerEmail
import ru.md.msc.db.user.repo.RoleRepository
import ru.md.msc.db.user.repo.UserRepository
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.user.biz.validate.isValidEmail
import ru.md.msc.domain.user.service.UserService

@SpringBootTest(classes = [TestBeans::class])
class UserTest(
	@Autowired private val deptService: DeptService,
	@Autowired private val userService: UserService,
	@Autowired private val baseUserService: BaseUserService,
	@Autowired private val roleRepository: RoleRepository,
	@Autowired private val userRepository: UserRepository,
) {

	@Test
	fun usersWithAwardTest() {
		val users = userService.getUsersWithAward(deptId = 3, baseQuery = BaseQuery())
		println(users)
	}

//	fun <T> equalsIgnoreOrder(list1:List<T>, list2:List<T>) = list1.size == list2.size && list1.toSet() == list2.toSet()

	@Test
	fun compareSets() {
			val set1 = setOf(RoleUser.USER, RoleUser.ADMIN)
			val set2 = setOf(RoleUser.ADMIN, RoleUser.USER)
		assertEquals(set1, set2)
	}

	@Test
	fun testLog() {
		log.error("Test from client")
	}

	@Test
	fun formatEmailValid() {
		val email = "sas22d@asd2.ru"
		val isValid = isValidEmail(email)
		assertEquals(true, isValid)
	}

	@Test
	fun formatEmailInvalid1() {
		val email = "sas22d@asd2."
		val isValid = isValidEmail(email)
		assertEquals(false, isValid)
	}

	@Test
	fun formatEmailInvalid2() {
		val email = "sas22d"
		val isValid = isValidEmail(email)
		assertEquals(false, isValid)
	}

	@Test
	fun authEmailExistTest() {
		val deptId = 3L
		val exist = userService.validateEmail(deptId = deptId, email = ownerEmail)
		assertEquals(true, exist)
	}

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
		assertEquals(1, count)
	}

	@Test
	fun ownerNotExistTest() {
		val count = roleRepository.countByUserIdAndRoleUser(
			userId = 2,
			roleUser = RoleUser.OWNER
		)
		assertEquals(0, count)
	}

	@Test
	@Transactional
	fun userToArchive() {
		val userId = 2L
		userRepository.moveUserToArchive(userId = userId)
		val user = baseUserService.findById(userId = userId)
		assertEquals(true, user?.archive)
	}

	companion object {
		private val log: Logger = LoggerFactory.getLogger(UserTest::class.java)
	}

}
