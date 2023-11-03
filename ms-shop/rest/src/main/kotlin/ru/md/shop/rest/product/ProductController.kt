@file:Suppress("KDocUnresolvedReference")

package ru.md.shop.rest.product

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.model.BaseResponse
import ru.md.base_rest.base.authProcess
import ru.md.base_rest.image.baseImageProcess
import ru.md.base_rest.model.mapper.toTransportBaseImageResponse
import ru.md.base_rest.model.request.AUTH
import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.base_rest.base.toLongOr0
import ru.md.base_rest.utils.JwtUtils
import ru.md.shop.domain.product.biz.proc.ProductCommand
import ru.md.shop.domain.product.biz.proc.ProductContext
import ru.md.shop.domain.product.biz.proc.ProductProcessor
import ru.md.shop.domain.product.model.Product
import ru.md.shop.rest.product.mappers.fromTransport
import ru.md.shop.rest.product.mappers.toTransportProductDetails
import ru.md.shop.rest.product.mappers.toTransportProducts
import ru.md.shop.rest.product.model.request.*
import ru.md.shop.rest.product.model.response.ProductDetailsResponse

@RestController
@RequestMapping("product")
class ProductController(
	private val productProcessor: ProductProcessor,
	private val jwtUtils: JwtUtils,
) {

	@PostMapping("create")
	private suspend fun create(
		@RequestBody request: CreateProductRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<ProductDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = productProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportProductDetails() }
		)
	}

	@PostMapping("update")
	private suspend fun update(
		@RequestBody request: UpdateProductRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<ProductDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = productProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportProductDetails() }
		)
	}

	@PostMapping("get_id")
	private suspend fun getById(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetProductByIdRequest
	): BaseResponse<ProductDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = productProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportProductDetails() }
		)
	}

	/**
	 * Получение списка призов.
	 * [deptId] - необходимо заполнить для Владельца, для определения конкретной компании.
	 * Может быть указан любой отдел компании, бэк сам определит id компании.
	 * Для всех остальных пользователей поле игнорируется (определяется автоматически).
	 *
	 * [maxPrice] - Фильтр по цене
	 * [available] - Фильтр по доступности (true- только в наличии, по умолчанию - false)
	 * [baseRequest]:
	 *  [filter] - фильтрация по имени приза (необязателен)
	 *  Параметры пагинации [page], [pageSize] - необязательны, по умолчанию 0 и 100 соответственно
	 *  Допустимые поля для сортировки:
	 *        "name",
	 *        "price",
	 *        "count",
	 *        "description",
	 */
	@PostMapping("get_company")
	private suspend fun getByDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetProductByCompanyRequest
	): BaseResponse<List<Product>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = productProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportProducts() }
		)
	}

	@PostMapping("delete")
	private suspend fun delete(
		@RequestBody request: DeleteProductRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<ProductDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = productProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportProductDetails() }
		)
	}

	@PostMapping("img_add")
	suspend fun imageAdd(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestPart("file") file: MultipartFile,
		@RequestPart("authId") authId: String,
		@RequestPart("productId") productId: String,
	): BaseResponse<BaseImageResponse> {
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		val context = ProductContext().apply { command = ProductCommand.IMG_ADD }
		return baseImageProcess(
			authData = authData,
			context = context,
			processor = productProcessor,
			multipartFile = file,
			authId = authId.toLongOr0(),
			entityId = productId.toLongOr0(),
		)
	}

	@PostMapping("img_delete")
	private suspend fun imageDelete(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: DeleteProductImageRequest
	): BaseResponse<BaseImageResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = productProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseImageResponse() }
		)
	}

}