package ru.md.msc.db.dept

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.domain.dept.service.DeptService

@SpringBootTest
class DeptTest(
	@Autowired private val deptRepository: DeptRepository,
	@Autowired private val deptService: DeptService
) {

	@Test
	fun hasUpTreeId() {
		val has = deptRepository.upTreeHasDeptId(59, 56)
		println("HAS: $has")
	}

	@Test
	@Transactional
	fun getSubtreeIds() {
		val ids = deptRepository.subTreeIds(81)
		println("ids: $ids")
	}

	@Test
	fun getSubtreeDepts() {
		val depts = deptService.findSubTreeDepts(81)
		println("depts: $depts")
	}

	@Test
	fun getByIdDetails() {
		val dept = deptService.findByIdDetails(deptId = 81)
		println("dept: $dept")
	}

	@Test
	fun getNearIds() {
		val ids = deptRepository.findChildIdsByParentId(parentId = 1)
		println("ids: $ids")
	}

	@Test
	@Transactional
	fun delete() {
		deptRepository.deleteById(90)
	}
}