package ru.md.msgal.rest.item

import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.model.BaseResponse
import ru.md.base_domain.model.baseResponse
import ru.md.base_rest.utils.AuthData
import ru.md.msgal.domain.item.biz.proc.ItemCommand
import ru.md.msgal.domain.item.biz.proc.ItemContext
import ru.md.msgal.domain.item.biz.proc.ItemProcessor
import ru.md.base_domain.gallery.GalleryItem
import ru.md.base_rest.base.emailNotVerified
import ru.md.base_rest.base.fileContentTypeError
import ru.md.base_rest.base.imageSaveError
import ru.md.base_rest.utils.ImageSaveException
import ru.md.base_rest.utils.saveImageFile
import java.io.File

// Допустимые типы файлов to возможность сжатия
private val mimes = listOf("image/jpeg" to true, "image/png" to true, "image/svg+xml" to false)

suspend fun addItemProc(
	authData: AuthData,
	itemProcessor: ItemProcessor,
	multipartFile: MultipartFile,
	folderId: Long,
	name: String,
	description: String?
	// Исправить на BaseImageResponse
): BaseResponse<GalleryItem> {

	val context = ItemContext()
	context.command = ItemCommand.CREATE

	if (!authData.emailVerified || authData.email.isBlank()) {
		context.emailNotVerified()
		return BaseResponse.error(errors = context.errors)
	}
	context.authEmail = authData.email

	val contentType = multipartFile.contentType
	val compress = mimes.find { it.first == contentType }?.second
	println("Content Type: $contentType, compress: $compress")
	if (compress == null) {
		context.fileContentTypeError(contentType ?: "", message = "Загружаемый файл должен быть изображением")
		return BaseResponse.error(errors = context.errors)
	}

	val fileData = try {
		saveImageFile(multipartFile = multipartFile, compress = compress)
	} catch (e: Exception) {
		when (e) {
			is ImageSaveException -> context.imageSaveError(e.message)
			else -> context.imageSaveError()
		}
		return BaseResponse.error(errors = context.errors)
	}

	context.fileData = fileData
	context.folderId = folderId
	context.item = GalleryItem(
		folderId = folderId,
		name = name,
		description = description
	)

	itemProcessor.exec(context)

	File(fileData.originUrl).delete()
	if (compress) {
		File(fileData.miniUrl).delete()
		if (fileData.normCompress) {
			File(fileData.normalUrl).delete()
		}
	}
	return context.baseResponse(data = context.item)
}