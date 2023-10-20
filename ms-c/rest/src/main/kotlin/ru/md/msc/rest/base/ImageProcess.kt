package ru.md.msc.rest.base

import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.base_rest.*
import ru.md.base_rest.model.mapper.toBaseImageResponse
import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.base_rest.utils.AuthData
import ru.md.msc.domain.base.biz.BaseClientContext
import java.io.File

// Допустимые типы файлов to возможность сжатия
private val mimes = listOf("image/jpeg" to true, "image/png" to true, "image/svg+xml" to false)

suspend fun <C : BaseClientContext> imageProcess(
	authData: AuthData,
	context: C,
	processor: IBaseProcessor<C>,
	multipartFile: MultipartFile,
	authId: Long,
	entityId: Long,
	imageId: Long = 0,
): BaseResponse<BaseImageResponse> {
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
		saveImageFile(multipartFile = multipartFile, entityId = entityId, compress = compress)
	} catch (e: Exception) {
		when (e) {
			is ImageSaveException -> context.imageSaveError(e.message)
			else -> context.imageSaveError()
		}
		return BaseResponse.error(errors = context.errors)
	}

	context.fileData = fileData
	context.authId = authId
	context.imageId = imageId

	processor.exec(context)
	File(fileData.originUrl).delete()
	if (compress) {
		File(fileData.miniUrl).delete()
		if (fileData.normCompress) {
			File(fileData.normalUrl).delete()
		}
	}
	return context.baseResponse(data = context.baseImage.toBaseImageResponse())
}