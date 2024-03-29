@file:Suppress("NonAsciiCharacters")

package ru.md.shop.db.pay.biz

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.base_db.pay.repo.BaseUserPayRepo
import ru.md.shop.db.TestBeans
import ru.md.shop.db.ownerEmail
import ru.md.shop.domain.pay.biz.proc.PayCommand
import ru.md.shop.domain.pay.biz.proc.PayContext
import ru.md.shop.domain.pay.biz.proc.PayProcessor
import ru.md.shop.domain.pay.model.PayCode
import ru.md.shop.domain.product.biz.proc.ProductCommand
import ru.md.shop.domain.product.biz.proc.ProductContext
import ru.md.shop.domain.product.biz.proc.ProductProcessor
import ru.md.shop.domain.product.model.Product
import ru.md.shop.domain.product.model.ProductDetails

@SpringBootTest(classes = [TestBeans::class])
class PayProductTest(
	@Autowired private val productProcessor: ProductProcessor,
	@Autowired private val payProcessor: PayProcessor,
	@Autowired private val baseUserPayRepo: BaseUserPayRepo,
) {

	@Test
	fun `Pay product by User and return by Admin after given`() = runBlocking {

		// Просмотр собственного баланса до покупки
		var payContext = PayContext().apply {
			authId = 2
			authEmail = ownerEmail
			command = PayCommand.GET_USER_PAY
		}
		payProcessor.exec(payContext)
		val initUserBalance = payContext.userPay.balance

		println(initUserBalance)

		val newProduct = Product(
			name = "test product",
			price = 100,
			count = 10
		)
		val productContext = ProductContext().apply {
			// Создаем новый продукт сотрудником с правами Администратора
			authId = 3
			authEmail = ownerEmail
			product = newProduct
			productDetails = ProductDetails(
				product = product,
				place = "test description"
			)
			command = ProductCommand.CREATE
		}
		productProcessor.exec(productContext)
		println(productContext.productDetails)
		val newProductId = productContext.productDetails.product.id

		payContext = PayContext().apply {
			// Покупаем продукт обычным сотрудником
			authId = 2
			authEmail = ownerEmail
			productId = newProductId
			command = PayCommand.PAY_PRODUCT
		}
		payProcessor.exec(payContext)
		val payData = payContext.payData

		assertEquals(PayCode.PAY, payData.payCode)
		assertEquals(newProductId, payData.product.id)
		assertEquals(newProduct.price, -payData.price)
		assertEquals(true, payData.isActive)

		// Просмотр собственного баланса (userId не нужен)
		payContext = PayContext().apply {
			authId = 2
			authEmail = ownerEmail
			command = PayCommand.GET_USER_PAY
		}
		payProcessor.exec(payContext)
		println("Свой баланс после покупки: ${payContext.userPay}")
		assertEquals(true, payContext.errors.size == 0)
		assertEquals(initUserBalance - newProduct.price, payContext.userPay.balance)

		payContext = PayContext().apply {
			authId = 1
			authEmail = ownerEmail
			userId = 2
			command = PayCommand.GET_USER_PAY
		}
		payProcessor.exec(payContext)
		assertEquals(true, payContext.errors.size == 0)

		payContext = PayContext().apply {
			// Выдаем продукт со склада Админом
			authId = 3
			authEmail = ownerEmail
			payDataId = payData.id
			command = PayCommand.ADMIN_GIVE_PRODUCT
		}
		payProcessor.exec(payContext)
		val givenPayData = payContext.payData

		payContext = PayContext().apply {
			// Возврат выданного продукта через Админа
			authId = 3
			authEmail = ownerEmail
			payDataId = givenPayData.id
			command = PayCommand.RETURN_PRODUCT
		}
		payProcessor.exec(payContext)

		// Просмотр собственного баланса (userId не нужен)
		// После возврата продукта должен быть равен начальному значению
		payContext = PayContext().apply {
			authId = 2
			authEmail = ownerEmail
			command = PayCommand.GET_USER_PAY
		}
		payProcessor.exec(payContext)
		assertEquals(true, payContext.errors.size == 0)
		assertEquals(initUserBalance, payContext.userPay.balance)
	}

	@Test
	fun getAdminPayDataTest() = runBlocking {
		val payContext = PayContext().apply {
			authId = 3
			authEmail = ownerEmail
			command = PayCommand.GET_PAYS_DATA
		}
		payProcessor.exec(payContext)
		println(payContext.errors)
		assertEquals(true, payContext.errors.size == 0)
	}

	@Test
	fun getOwnerPayDataTest() = runBlocking {
		val payContext = PayContext().apply {
			authId = 1
			authEmail = ownerEmail
			deptId = 3
			command = PayCommand.GET_PAYS_DATA
		}
		payProcessor.exec(payContext)
		println(payContext.errors)
		assertEquals(true, payContext.errors.size == 0)
	}

	@Test
	fun getUserWithIdPayDataTest() = runBlocking {
		val payContext = PayContext().apply {
			authId = 3
			authEmail = ownerEmail
			userId = 2
			command = PayCommand.GET_USER_PAY
		}
		payProcessor.exec(payContext)
		assertEquals(true, payContext.errors.size == 0)
	}

	@Test
	fun getUserPayDataTest() = runBlocking {
		val payContext = PayContext().apply {
			authId = 2
			authEmail = ownerEmail
			command = PayCommand.GET_USER_PAY
		}
		payProcessor.exec(payContext)
		assertEquals(true, payContext.errors.size == 0)
	}

	@Test
	fun getUserPayTest()   {
		val userPayEntity = baseUserPayRepo.findByUserId(1)
		println(userPayEntity)
	}
}