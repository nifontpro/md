package ru.md.msc.db.tc.dept

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.db.tc.TestBeans
import ru.md.msc.domain.dept.service.DeptService

@SpringBootTest(classes = [TestBeans::class])
class DeptTest(
	@Autowired private val deptService: DeptService,
	@Autowired private val deptRepo: DeptRepository
) {

	@Test
	fun deptExistTest() {
		val exist = deptService.checkDeptExist(parentId = 2, "Company 3, id=3")
		println(exist)
		assertEquals(true, exist)
	}

	@Test
	@Transactional
	fun testLazyFormula() {
		val dept = deptRepo.findByIdOrNull(5)
		println(dept)
		println(dept?.company?.name)
	}
}