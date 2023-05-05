package ru.md.msc.db.award

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.award.repo.ActivityRepository
import ru.md.msc.db.award.repo.AwardRepository
import ru.md.msc.domain.award.service.AwardService
import ru.md.msc.domain.base.model.converter.toLocalDateTimeUTC

@SpringBootTest
class AwardTest(
	@Autowired private val awardRepository: AwardRepository,
	@Autowired private val activityRepository: ActivityRepository,
	@Autowired private val awardService: AwardService,
) {

	@Test
	fun findDeptId() {
		val deptId = awardRepository.finDeptId(awardId = 9)
		println("DeptId: $deptId")
	}

	@Test
	fun findActByUser() {
		val acts = awardService.findActivAwardsByUser(userId = 217)
		println("Res: $acts")
	}

	@Test
	fun findActByDept() {
		val ld = (1683316491002L).toLocalDateTimeUTC()
		val acts = activityRepository.findByDeptId(
			deptId = 87,
			minDate = ld,
			maxDate = null
		)
		println("Res: $acts")
	}

}