package ru.md.msc.db.dept

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.dept.repo.DeptRepository

@SpringBootTest
class DeptTest(
	@Autowired private val deptRepository: DeptRepository
) {

	@Test
	fun hasUpTreeId() {
		val has = deptRepository.upTreeHasDeptId(59, 56)
		println("HAS: $has")
	}
}