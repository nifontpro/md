package ru.md.msc.db.tc.user

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.base_db.user.model.converter.toGender
import ru.md.base_domain.user.model.Gender
import ru.md.msc.db.tc.TestBeans
import ru.md.msc.db.user.repo.UserDetailsRepository

@SpringBootTest(classes = [TestBeans::class])
class GetGenderTest(
	@Autowired private val userDetailsRepository: UserDetailsRepository,
) {

	@Test
	fun getGenderMaleTest() {
		val gender = userDetailsRepository.getGenderByName("Алексей", "").toGender()
		assertEquals(Gender.MALE, gender)
	}

	@Test
	fun getGenderMaleLastnameTest() {
		val gender = userDetailsRepository.getGenderByName("Нет", "Абалдуев").toGender()
		assertEquals(Gender.MALE, gender)
	}

	@Test
	fun getGenderFemaleTest() {
		val gender = userDetailsRepository.getGenderByName("Агния", "").toGender()
		assertEquals(Gender.FEMALE, gender)
	}

	@Test
	fun getGenderUndefTest() {
		val gender = userDetailsRepository.getGenderByName("Агн", "Брр").toGender()
		assertEquals(Gender.UNDEF, gender)
	}
}
