package ru.md.shop.db.product

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.base_domain.model.BaseQuery
import ru.md.shop.db.TestBeans
import ru.md.shop.db.ownerEmail
import ru.md.shop.domain.product.biz.proc.ProductCommand
import ru.md.shop.domain.product.biz.proc.ProductContext
import ru.md.shop.domain.product.biz.proc.ProductProcessor
import ru.md.shop.domain.product.model.Product
import ru.md.shop.domain.product.model.ProductDetails

@SpringBootTest(classes = [TestBeans::class])
class ProductTest(
	@Autowired private val productProcessor: ProductProcessor,
) {

	@Test
	fun getByCompanyTest() = runBlocking {
		var productContext = ProductContext().apply {
			// Создаем новый продукт сотрудником с правами Администратора
			authId = 3
			authEmail = ownerEmail
			product = Product(
				name = "Test product 1",
				price = 100,
				count = 10
			)
			productDetails = ProductDetails(
				product = product,
				place = "place 1"
			)
			command = ProductCommand.CREATE
		}
		productProcessor.exec(productContext)
		val newProduct1Id = productContext.productDetails.product.id

		productContext = ProductContext().apply {
			// Создаем новый продукт сотрудником с правами Администратора
			authId = 3
			authEmail = ownerEmail
			product = Product(
				name = "Test Product 2",
				price = 50,
				count = 0
			)
			productDetails = ProductDetails(
				product = product,
				place = "place 2"
			)
			command = ProductCommand.CREATE
		}
		productProcessor.exec(productContext)

		productContext = ProductContext().apply {
			// Создаем новый продукт сотрудником с правами Администратора
			authId = 3
			authEmail = ownerEmail
			product = Product(
				name = "Test Product 3",
				price = 30,
				count = 3
			)
			productDetails = ProductDetails(
				product = product,
				place = "place 3"
			)
			command = ProductCommand.CREATE
		}
		productProcessor.exec(productContext)
		val newProduct2Id = productContext.productDetails.product.id

		productContext = ProductContext().apply {
			authId = 2
			authEmail = ownerEmail

			baseQuery = BaseQuery(
				filter = "test pro"
			)

			command = ProductCommand.GET_BY_COMPANY
		}
		productProcessor.exec(productContext)
		assertEquals(3, productContext.products.size)
		assertEquals(1, productContext.products.count { it.id == newProduct1Id })
		assertEquals(1, productContext.products.count { it.id == newProduct2Id })

		productContext = ProductContext().apply {
			// Создаем новый продукт сотрудником с правами Администратора
			authId = 2
			authEmail = ownerEmail

			baseQuery = BaseQuery(
				filter = "test pro"
			)
			available = true

			command = ProductCommand.GET_BY_COMPANY
		}
		productProcessor.exec(productContext)
		assertEquals(2, productContext.products.size)

		productContext = ProductContext().apply {
			// Создаем новый продукт сотрудником с правами Администратора
			authId = 2
			authEmail = ownerEmail

			baseQuery = BaseQuery(
				filter = "test pro"
			)
			available = true
			maxPrice = 30

			command = ProductCommand.GET_BY_COMPANY
		}
		productProcessor.exec(productContext)
		assertEquals(1, productContext.products.size)

	}

}