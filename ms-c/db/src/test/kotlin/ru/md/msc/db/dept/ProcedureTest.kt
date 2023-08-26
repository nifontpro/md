package ru.md.msc.db.dept

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.dept.repo.DeptRepository

@SpringBootTest
@Transactional
class ProcedureTest(
	@Autowired private val deptRepository: DeptRepository
) {

	@Test
	fun testSubTreeIds() {
		val deptIds = deptRepository.subTreeIds(1)
		println(deptIds)
	}
}