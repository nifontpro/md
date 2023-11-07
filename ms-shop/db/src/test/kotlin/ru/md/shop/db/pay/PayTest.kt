package ru.md.shop.db.pay

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import ru.md.shop.db.TestBeans

@SpringBootTest(classes = [TestBeans::class])
class PayTest {

	companion object {
		@JvmStatic
		@AfterAll
		fun afterAllTest() {
			println("Tests end.")
		}

		@JvmStatic
		@BeforeAll
		fun before() {
			println("Before tests")
		}
	}


	@Test
	fun test() {
		assertEquals(1, 1)
	}
}