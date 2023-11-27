package ru.md.msc.db.tc.award

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.base_domain.model.BaseQuery
import ru.md.msc.db.tc.TestBeans
import ru.md.msc.domain.award.service.AwardService

@SpringBootTest(classes = [TestBeans::class])
class AwardTest(
	@Autowired private val awardService: AwardService,
) {

	@Test
	fun usersWithAwardTest() {
		val awards = awardService.findBySubDept(
			deptId = 3,
			awardState = null,
			baseQuery = BaseQuery()
		)
		println(awards)
	}

	@Test
	fun activityTest() {
		val acts = awardService.findActivAwardsByUser(
			userId = 2L,
			awardState = null,
			awardType = null,
			baseQuery = BaseQuery()
		)
		println(acts)
	}

}