package ru.md.msc.db.award

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.award.repo.AwardRepository

@SpringBootTest
class AwardTest(
	@Autowired private val awardRepository: AwardRepository,
) {

	@Test
	fun findDeptId() {
		val deptId = awardRepository.finDeptId(awardId = 9)
		println("DeptId: $deptId")
	}

}