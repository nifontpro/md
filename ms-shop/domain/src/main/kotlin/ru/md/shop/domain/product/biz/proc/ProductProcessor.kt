package ru.md.shop.domain.product.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.base_domain.client.MicroClient
import ru.md.cor.rootChain
import ru.md.shop.domain.product.service.ProductService

@Component
class ProductProcessor(
	private val productService: ProductService,
	private val microClient: MicroClient,
) : IBaseProcessor<ProductContext> {

	override suspend fun exec(ctx: ProductContext) = businessChain.exec(ctx.also {
		it.productService = productService
//		it.deptService = deptService
//		it.medalService = medalService
//		it.s3Repository = s3Repository
//		it.microClient = microClient
//		it.messageService = messageService
	})

	companion object {

		private val businessChain = rootChain<ProductContext> {
			initStatus()

			operation("Создать приз", ProductCommand.CREATE) {

			}

			operation("Обновить приз", ProductCommand.UPDATE) {


			}

			operation("Получить по id", ProductCommand.GET_BY_ID) {

			}

			operation("Удалить приз", ProductCommand.DELETE) {

			}

			operation("Добавление изображения", ProductCommand.IMG_ADD) {
//				worker("Получение id сущности") { medalId = fileData.entityId }
//				validateProductIdAndAccessToProductChain()
//				prepareProductImagePrefixUrl("Получаем префикс изображения")
//				addImageToS3("Сохраняем изображение в s3")
//				addProductImageToDb("Сохраняем ссылки на изображение в БД")
//				updateProductMainImage("Обновление основного изображения")
//				deleteS3ImageOnFailingChain()
//				getProductByIdDetails("Получаем медаль с детализацией")
			}

			operation("Удаление изображения", ProductCommand.IMG_DELETE) {
//				validateImageId("Проверка imageId")
//				validateProductIdAndAccessToProductChain()
//				deleteProductImageFromDb("Удаляем изображение из БД")
//				deleteBaseImageFromS3("Удаляем изображение из s3")
//				updateProductMainImage("Обновление основного изображения")
			}

			finishOperation()
		}.build()
	}
}