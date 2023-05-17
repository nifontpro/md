package ru.md.msgal.rest.item

import org.springframework.web.multipart.MultipartFile
import ru.md.base_rest.emailNotVerified
import ru.md.base_rest.fileSaveError
import ru.md.base_rest.model.BaseResponse
import ru.md.base_rest.model.baseResponse
import ru.md.base_rest.saveFile
import ru.md.base_rest.utils.AuthData
import ru.md.msgal.domain.item.biz.proc.ItemContext
import ru.md.msgal.domain.item.biz.proc.ItemProcessor
import ru.md.msgal.domain.item.model.Item
import java.io.File

suspend fun addItemProc(
	authData: AuthData,
	itemProcessor: ItemProcessor,
	file: MultipartFile,
	folderId: Long,
	name: String,
	description: String
): BaseResponse<Item> {

	val context = ItemContext()

	if (!authData.emailVerified || authData.email.isBlank()) {
		context.emailNotVerified()
		return BaseResponse.error(errors = context.errors)
	}
	context.authEmail = authData.email

	val fileData = saveFile(multipartFile = file) ?: run {
		context.fileSaveError()
		return BaseResponse.error(errors = context.errors)
	}

	context.fileData = fileData
	context.folderId = folderId
	context.item = Item(
		folderId = folderId,
		name = name,
		description = description
	)

	itemProcessor.exec(context)

	File(fileData.url).delete()

	return context.baseResponse(data = context.item)
}