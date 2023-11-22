package ru.md.msgal.rest.item

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.gallery.GalleryItem
import ru.md.base_domain.gallery.SmallItem
import ru.md.base_domain.gallery.request.GetItemByIdRequest
import ru.md.base_domain.model.BaseResponse
import ru.md.base_rest.base.authProcess
import ru.md.base_rest.model.request.AUTH
import ru.md.base_rest.base.process
import ru.md.base_rest.base.toLongOr0
import ru.md.base_rest.utils.JwtUtils
import ru.md.msgal.domain.item.biz.proc.ItemProcessor
import ru.md.msgal.rest.item.mappers.fromTransport
import ru.md.msgal.rest.item.mappers.toTransportItems
import ru.md.msgal.rest.item.mappers.toTransportSmallItem
import ru.md.msgal.rest.item.mappers.toTransportSmallItemJson
import ru.md.msgal.rest.item.model.request.GetItemsByFolderRequest
import ru.md.msgal.rest.item.model.response.ItemResponse

@RestController
@RequestMapping("item")
class ItemController(
	private val itemProcessor: ItemProcessor,
	private val jwtUtils: JwtUtils,
) {

	@PostMapping("admin/create")
	private suspend fun create(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestPart("file") file: MultipartFile,
		@RequestPart("folderId") folderId: String,
		@RequestPart("name") name: String,
		@RequestPart("description") description: String?,
	): BaseResponse<GalleryItem> {
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		return addItemProc(
			authData = authData,
			itemProcessor = itemProcessor,
			multipartFile = file,
			folderId = folderId.toLongOr0(),
			name = name,
			description = description
		)
	}

	/**
	 * Получение объектов из папки галереи
	 * baseRequest:
	 *  Параметры пагинации page, pageSize - необязательны, по умолчанию 0 и 100 соответственно
	 *  Допустимые поля для сортировки:
	 *  			"name",
	 *  			"createdAt",
	 */
	@PostMapping("get_folder")
	private suspend fun getItemsByFolder(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetItemsByFolderRequest
	): BaseResponse<List<ItemResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = itemProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportItems() }
		)
	}

	@PostMapping("get_id")
	private suspend fun getItemById(
		@RequestBody request: GetItemByIdRequest
	): BaseResponse<String> {
		return process(
			processor = itemProcessor,
			request = request,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportSmallItemJson() }
		)
	}

	@PostMapping("get")
	private suspend fun getItem(
		@RequestBody request: GetItemByIdRequest
	): BaseResponse<SmallItem> {
		return process(
			processor = itemProcessor,
			request = request,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportSmallItem() }
		)
	}

}