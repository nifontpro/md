package ru.md.msc.db.tc

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.domain.dept.service.DeptService

@SpringBootTest(classes = [TestBeans::class])
@AutoConfigureMockMvc
@Transactional
class TcTest(
	@Autowired private val deptService: DeptService,
	@Autowired private val deptRepository: DeptRepository,
	@Autowired private val deptDetailsRepository: DeptDetailsRepository
) {

	@Test
	fun deptTest() {
		val deptsDetails = deptDetailsRepository.findAll()
		println(deptsDetails)
		val depts = deptService.findSubTreeDepts(1)
		println(depts)
	}
}
