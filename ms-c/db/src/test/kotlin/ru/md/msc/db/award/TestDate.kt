package ru.md.msc.db.award

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.domain.base.model.converter.toEpochMilliUTC
import ru.md.msc.domain.base.model.converter.toLocalDateTimeUTC
import java.time.LocalDateTime

@SpringBootTest
class TestDate {
	@Test
	fun testDate() {
		val now = LocalDateTime.now()
		println(now)
		val nowMs = now.toEpochMilliUTC()
		println("ms:  $nowMs")
		val sys = System.currentTimeMillis()
		println("sys: $sys")
		val o = nowMs.toLocalDateTimeUTC()
		assertEquals(now, o)
	}
}