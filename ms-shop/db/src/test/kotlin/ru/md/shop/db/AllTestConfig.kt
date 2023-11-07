package ru.md.shop.db

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.base_domain.pay.service.BaseUserPayService
import ru.md.shop.domain.pay.service.PayService


@SpringBootTest(classes = [TestBeans::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BeforeAndAfterAnnotationsUnitTest(
	@Autowired private val baseUserPayService: BaseUserPayService,
	@Autowired private val payService: PayService,
) {

	@BeforeAll
	fun setup() {
		println("BEFORE")
	}

	@AfterAll
	fun teardown() {
		println("AFTER")
		val ub = payService.getUserPayData(userId = 3)
		println("Balance = ${ub.balance}")
	}

	@Test
	fun changeBalanceTest() {
		val userId = 3L
		val initBalance = payService.getUserPayData(userId = userId).balance

		baseUserPayService.changeBalance(
			userId = userId, delta = 100
		)
		baseUserPayService.changeBalance(
			userId = userId, delta = 50
		)
		baseUserPayService.changeBalance(
			userId = userId, delta = -10
		)
		val userPay = payService.getUserPayData(userId = userId)
		Assertions.assertEquals(initBalance + 100 + 50 - 10, userPay.balance)
	}
}