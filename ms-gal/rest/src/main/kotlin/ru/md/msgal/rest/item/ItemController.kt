package ru.md.msgal.rest.item

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.base_rest.model.AUTH
import ru.md.base_rest.model.BaseResponse
import ru.md.base_rest.process
import ru.md.base_rest.toLongOr0
import ru.md.base_rest.utils.JwtUtils
import ru.md.msgal.domain.item.biz.proc.ItemProcessor
import ru.md.msgal.domain.item.model.Item
import ru.md.msgal.rest.item.mappers.fromTransport
import ru.md.msgal.rest.item.mappers.toTransportItems
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
	): BaseResponse<Item> {
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		return addItemProc(
			authData = authData,
			itemProcessor = itemProcessor,
			file = file,
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
		return process(
			processor = itemProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportItems() }
		)
	}
}