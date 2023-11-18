package ru.md.msc.db.tc.user

import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TestDateConverter {

	@Test
	fun converterTest() {
		val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
//		val date = formatter.parse("03.02.1981")
		val date = LocalDate.parse("03.02.1981", formatter)
		println(date.atStartOfDay())
	}

}