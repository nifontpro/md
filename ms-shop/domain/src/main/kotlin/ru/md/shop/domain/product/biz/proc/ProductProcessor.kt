package ru.md.shop.domain.product.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.dept.biz.validate.validateDeptId
import ru.md.base_domain.image.biz.validate.validateImageId
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.base_domain.dept.service.BaseDeptService
import ru.md.base_domain.image.biz.chain.deleteS3ImageOnFailingChain
import ru.md.base_domain.image.biz.workers.addImageToS3
import ru.md.base_domain.image.biz.workers.deleteBaseImageFromS3
import ru.md.base_domain.s3.repo.BaseS3Repository
import ru.md.cor.ICorChainDsl
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.shop.domain.product.biz.validate.validateProductId
import ru.md.shop.domain.product.biz.validate.validateProductName
import ru.md.shop.domain.product.biz.validate.validateProductPrice
import ru.md.shop.domain.product.biz.workers.*
import ru.md.shop.domain.product.service.ProductService

@Component
class ProductProcessor(
	private val productService: ProductService,
	private val baseDeptService: BaseDeptService,
	private val baseS3Repository: BaseS3Repository,
//	private val microClient: MicroClient,
) : IBaseProcessor<ProductContext> {

	override suspend fun exec(ctx: ProductContext) = businessChain.exec(ctx.also {
		it.productService = productService
		it.baseDeptService = baseDeptService
		it.baseS3Repository = baseS3Repository
//		it.microClient = microClient
	})

	companion object {

		private val businessChain = rootChain<ProductContext> {
			initStatus()

			operation("Создать приз", ProductCommand.CREATE) {
				validateDeptId("Проверяем deptId")
				validateAndTrimFields()
				// Auth
				createProduct("Создаем приз")
			}

			operation("Обновить приз", ProductCommand.UPDATE) {
				validateProductId("Проверяем productId")
				validateAndTrimFields()
				// Auth
				updateProduct("Обновляем приз")
			}

			operation("Получить по id", ProductCommand.GET_BY_ID) {
				validateProductId("Проверяем productId")
				getProductDetailsById("Получаем приз")
			}

			operation("Получить призы в отделе", ProductCommand.GET_BY_DEPT) {
				validateDeptId("Проверяем deptId")
				getProductsByDept("Получаем призы отдела")
			}

			operation("Удалить приз", ProductCommand.DELETE) {
				validateProductId("Проверяем productId")
				getProductDetailsById("Получаем приз")
				deleteProduct("Удаляем приз")
			}

			operation("Добавление изображения", ProductCommand.IMG_ADD) {
				worker("Получение id сущности") { productId = fileData.entityId }
				validateProductId("Проверяем productId")
//				validateProductIdAndAccessToProductChain()
				prepareProductImagePrefixUrl("Получаем префикс изображения")
				addImageToS3("Сохраняем изображение в s3")
				addProductImageToDb("Сохраняем ссылки на изображение в БД")
				updateProductMainImage("Обновление основного изображения")
				deleteS3ImageOnFailingChain()
				getProductDetailsById("Получаем приз")
			}

			operation("Удаление изображения", ProductCommand.IMG_DELETE) {
				validateImageId("Проверка imageId")
				validateProductId("Проверяем productId")
//				validateProductIdAndAccessToProductChain()
				deleteProductImageFromDb("Удаляем изображение из БД")
				deleteBaseImageFromS3("Удаляем изображение из s3")
				updateProductMainImage("Обновление основного изображения")
			}

			finishOperation()
		}.build()

		private fun ICorChainDsl<ProductContext>.validateAndTrimFields() {
			validateProductName("Проверяем название")
			validateProductPrice("Проверяем цену")
			trimFieldProductDetails("Очищаем поля")
		}
	}
}