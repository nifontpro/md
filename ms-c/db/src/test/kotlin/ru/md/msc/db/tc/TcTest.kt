package ru.md.msc.db.tc

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.base_domain.model.BaseOrder
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.Direction
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.user.service.UserService

@SpringBootTest(classes = [TestBeans::class])
@Transactional
class TcTest(
	@Autowired private val deptService: DeptService,
	@Autowired private val userService: UserService,
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
}
