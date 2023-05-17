package ru.md.msgal.rest.item

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.base_rest.model.AUTH
import ru.md.base_rest.model.BaseResponse
import ru.md.base_rest.toLongOr0
import ru.md.base_rest.utils.JwtUtils
import ru.md.msgal.domain.item.biz.proc.ItemProcessor
import ru.md.msgal.domain.item.model.Item

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
}