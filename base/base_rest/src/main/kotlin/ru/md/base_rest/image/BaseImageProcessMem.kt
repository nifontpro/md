package ru.md.base_rest.image

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.model.BaseResponse
import ru.md.base_domain.model.baseResponse
import ru.md.base_rest.base.emailNotVerified
import ru.md.base_rest.base.fileContentTypeError
import ru.md.base_rest.base.imageSaveError
import ru.md.base_rest.model.mapper.toBaseImageResponse
import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.base_rest.utils.AuthData

data class ImageType(
	val contentType: String,
	val format: String,
	val compress: Boolean
)

// Допустимые типы файлов to возможность сжатия
private val mimes = listOf(
	ImageType(contentType = "image/jpeg", format = "jpg", compress = true),
	ImageType(contentType = "image/png", format = "png", compress = true),
	ImageType(contentType = "image/svg+xml", format = "svg", compress = false),
)

suspend fun <C : BaseMedalsContext> baseImageProcessMem(
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
	val imageType = mimes.find { it.contentType == contentType }
	println("Content Type: $contentType")
	if (imageType == null) {
		context.fileContentTypeError(contentType ?: "", message = "Загружаемый файл должен быть изображением")
		return BaseResponse.error(errors = context.errors)
	}

	context.imageData = try {
		compressImage(multipartFile = multipartFile, entityId = entityId, imageType = imageType)
	} catch (e: Exception) {
		when (e) {
			is ConvertImageException -> context.imageSaveError(e.message)
			else -> context.imageSaveError()
		}
		return BaseResponse.error(errors = context.errors)
	}

	context.authId = authId
	context.imageId = imageId

	processor.exec(context)

	withContext(Dispatchers.IO) {
		context.imageData.originImage?.first?.close()
		if (context.imageData.compress) {
			context.imageData.miniImage?.first?.close()
			if (context.imageData.normCompress) {
				context.imageData.normalImage?.first?.close()
			}
		}
	}

	return context.baseResponse(data = context.baseImage.toBaseImageResponse())
}