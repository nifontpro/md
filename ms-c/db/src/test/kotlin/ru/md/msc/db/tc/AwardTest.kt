package ru.md.msc.db.tc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.award.repo.ActivityRepository

@SpringBootTest(classes = [TestBeans::class])
class AwardTest(
	@Autowired private val activityRepository: ActivityRepository,
) {

	@Test
	fun activityByUserExist() {
		val countExist = activityRepository.countByUserIdAndActiv(userId = 2) > 0
		assertEquals(true, countExist)
	}

	@Test
	fun activityByUserNotExist() {
		val countExist = activityRepository.countByUserIdAndActiv(userId = 3) > 0
		assertEquals(false, countExist)
	}
}
