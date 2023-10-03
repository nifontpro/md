package ru.md.msc.db.tc.dept

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.tc.TestBeans
import ru.md.msc.domain.dept.service.DeptService

@SpringBootTest(classes = [TestBeans::class])
class DeptTest(
	@Autowired private val deptService: DeptService,
) {

	@Test
	fun deptExistTest() {
		val exist = deptService.checkDeptExist(parentId = 2, "Dept 3, id=3")
		println(exist)
		assertEquals(true, exist)
	}
}