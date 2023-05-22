package ru.md.msc.db.award

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.award.repo.AwardRepository
import ru.md.msc.domain.award.service.AwardService
import org.junit.jupiter.api.Assertions.assertEquals

@SpringBootTest
class AwardTest(
	@Autowired private val awardRepository: AwardRepository,
	@Autowired private val awardService: AwardService,
) {

	@Test
	fun findDeptId() {
		val deptId = awardRepository.findDeptId(awardId = 9)
		println("DeptId: $deptId")
	}

	@Test
	fun findActByUser() {
		val acts = awardService.findActivAwardsByUser(userId = 217)
		println("Res: $acts")
	}

	@Test
	fun countByDeptId() {
		val c87 = awardRepository.countByDeptId(87)
		println("c87 = $c87")
		val c81 = awardRepository.countByDeptId(81)
		println("c81 = $c81")
		val c2 = awardRepository.countByDeptIdIn(listOf(81, 87))
		println("c2 = $c2")
		assertEquals(c2, c81 + c87)
	}

}