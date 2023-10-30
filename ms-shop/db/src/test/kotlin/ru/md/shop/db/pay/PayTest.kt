package ru.md.shop.db.pay

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.base_domain.pay.service.BaseUserPayService
import ru.md.shop.db.TestBeans
import ru.md.shop.domain.pay.service.UserPayService

@SpringBootTest(classes = [TestBeans::class])
class PayTest(
	@Autowired private val baseUserPayService: BaseUserPayService,
	@Autowired private val userPayService: UserPayService,
) {

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

	@Test
	fun changeBalanceTest() {
		val userId = 3L
		baseUserPayService.changeBalance(
			userId = userId, delta = 100
		)
		baseUserPayService.changeBalance(
			userId = userId, delta = 50
		)
		baseUserPayService.changeBalance(
			userId = userId, delta = -10
		)
		val userPay = userPayService.getPayData(userId = userId)
		assertEquals(140, userPay.balance)
	}
}