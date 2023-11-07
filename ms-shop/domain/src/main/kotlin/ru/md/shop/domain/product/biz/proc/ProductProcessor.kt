package ru.md.shop.domain.product.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.validate.chain.validatePageParamsChain
import ru.md.base_domain.biz.validate.validateAdminRole
import ru.md.base_domain.biz.validate.validateSortedFields
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.base_domain.dept.service.BaseDeptService
import ru.md.base_domain.image.biz.chain.deleteS3ImageOnFailingChain
import ru.md.base_domain.image.biz.validate.validateImageId
import ru.md.base_domain.image.biz.workers.addImageToS3
import ru.md.base_domain.image.biz.workers.deleteBaseImageFromS3
import ru.md.base_domain.s3.repo.BaseS3Repository
import ru.md.base_domain.user.biz.workers.getAuthUserAndVerifyEmail
import ru.md.base_domain.user.service.BaseUserService
import ru.md.cor.ICorChainDsl
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.shop.domain.base.biz.validate.chain.validateProductIdAndAdminAccessToProductChain
import ru.md.shop.domain.base.biz.validate.validateProductId
import ru.md.shop.domain.base.biz.workers.findCompanyDeptIdByOwnerOrAuthUserChain
import ru.md.shop.domain.base.service.BaseProductService
import ru.md.shop.domain.product.biz.validate.validateProductCount
import ru.md.shop.domain.product.biz.validate.validateProductName
import ru.md.shop.domain.product.biz.validate.validateProductPrice
import ru.md.shop.domain.product.biz.workers.*
import ru.md.shop.domain.product.service.ProductService

@Component
class ProductProcessor(
	private val productService: ProductService,
	private val baseProductService: BaseProductService,
	private val baseDeptService: BaseDeptService,
	private val baseUserService: BaseUserService,
	private val baseS3Repository: BaseS3Repository,
) : IBaseProcessor<ProductContext> {

	override suspend fun exec(ctx: ProductContext) = businessChain.exec(ctx.also {
		it.productService = productService
		it.baseProductService = baseProductService
		it.baseDeptService = baseDeptService
		it.baseUserService = baseUserService
		it.baseS3Repository = baseS3Repository
	})

	companion object {

		private val businessChain = rootChain<ProductContext> {
			initStatus()

			operation("Создать приз", ProductCommand.CREATE) {
				validateAndTrimProductFields()
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAdminRole("Проверка наличия прав Администратора")
				findCompanyDeptIdByOwnerOrAuthUserChain()
				createProduct("Создаем приз")
			}

			operation("Обновить приз", ProductCommand.UPDATE) {
				validateAndTrimProductFields()
				validateProductIdAndAdminAccessToProductChain()
				updateProduct("Обновляем приз")
			}

			operation("Получить по id", ProductCommand.GET_BY_ID) {
				validateProductId("Проверяем productId")
				getProductDetailsById("Получаем приз")
			}

			operation("Получить призы в компании", ProductCommand.GET_BY_COMPANY) {
				validatePageParamsChain()
				setProductByCompanySortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверяем сортировочные поля")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				findCompanyDeptIdByOwnerOrAuthUserChain()
				getProductsByDept("Получаем призы компании")
			}

			operation("Удалить приз", ProductCommand.DELETE) {
				validateProductIdAndAdminAccessToProductChain()
				deleteProduct("Удаляем приз")
			}

			operation("Добавление изображения", ProductCommand.IMG_ADD) {
				worker("Получение id сущности") { productId = fileData.entityId }
				validateProductIdAndAdminAccessToProductChain()
				prepareProductImagePrefixUrl("Получаем префикс изображения")
				addImageToS3("Сохраняем изображение в s3")
				addProductImageToDb("Сохраняем ссылки на изображение в БД")
				updateProductMainImage("Обновление основного изображения")
				deleteS3ImageOnFailingChain()
				getProductDetailsById("Получаем приз")
			}

			operation("Удаление изображения", ProductCommand.IMG_DELETE) {
				validateImageId("Проверка imageId")
				validateProductIdAndAdminAccessToProductChain()
				deleteProductImageFromDb("Удаляем изображение из БД")
				deleteBaseImageFromS3("Удаляем изображение из s3")
				updateProductMainImage("Обновление основного изображения")
			}

			finishOperation()
		}.build()

		private fun ICorChainDsl<ProductContext>.validateAndTrimProductFields() {
			validateProductName("Проверяем название")
			validateProductPrice("Проверяем цену")
			trimFieldProductDetails("Очищаем поля")
			validateProductCount("Проверяем количество")
		}
	}
}