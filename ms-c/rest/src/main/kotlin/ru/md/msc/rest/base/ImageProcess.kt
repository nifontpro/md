package ru.md.msc.rest.base

import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.base_rest.emailNotVerified
import ru.md.base_rest.fileContentTypeError
import ru.md.base_rest.fileSaveError
import ru.md.base_rest.saveFile
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
	// Исправить на BaseImageResponsecfg
): BaseResponse<BaseImage> {
	if (!authData.emailVerified || authData.email.isBlank()) {
		context.emailNotVerified()
		return BaseResponse.error(errors = context.errors)
	}
	context.authEmail = authData.email

	val contentType = multipartFile.contentType
	val compress = mimes.find { it.first == contentType }?.second
	println("Content Type: $contentType, compress: $compress")
	if (compress == null) {
		context.fileContentTypeError(contentType ?: "")
		return BaseResponse.error(errors = context.errors)
	}

	val fileData = saveFile(multipartFile = multipartFile, entityId = entityId, compress = compress) ?: run {
		context.fileSaveError()
		return BaseResponse.error(errors = context.errors)
	}

	context.fileData = fileData
	context.authId = authId
	context.imageId = imageId

	processor.exec(context)

	File(fileData.imageUrl).delete()
	File(fileData.miniUrl).delete()

	return context.baseResponse(data = context.baseImage)
}