package ru.md.msc.db.award

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.award.repo.ActivityRepository
import ru.md.msc.db.award.repo.AwardRepository
import ru.md.msc.domain.award.service.AwardService
import java.time.LocalDateTime

@SpringBootTest
class AwardTest(
	@Autowired private val awardRepository: AwardRepository,
	@Autowired private val activityRepository: ActivityRepository,
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

	@Test
	fun actCount() {
		val res = activityRepository.getActivAwardCountByDept(
			deptsIds = listOf(81, 87),
			minDate = LocalDateTime.of(2023, 5, 10, 0, 0, 0)
		)
		println(res)
	}

	@Test
	fun allActCount() {
		val res = activityRepository.getAllCountByDept(
			deptsIds = listOf(81, 87),
			minDate = LocalDateTime.of(2023, 5, 10, 0, 0, 0)
		)
		println(res)
	}

}