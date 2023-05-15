package ru.md.base_rest

import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.biz.proc.BaseContext
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_rest.model.BaseResponse
import ru.md.base_rest.model.baseResponse
import ru.md.base_rest.utils.AuthData
import java.io.File

suspend fun <C : BaseContext> imageProcess(
	authData: AuthData,
	context: C,
	processor: IBaseProcessor<C>,
	multipartFile: MultipartFile,
	authId: Long,
	entityId: Long,
	imageId: Long = 0,
): BaseResponse<BaseImage> {
	if (!authData.emailVerified || authData.email.isBlank()) {
		context.emailNotVerified()
		return BaseResponse.error(errors = context.errors)
	}
	context.authEmail = authData.email

	val fileData = saveFile(multipartFile = multipartFile, entityId = entityId) ?: run {
		context.fileSaveError()
		return BaseResponse.error(errors = context.errors)
	}
	context.fileData = fileData
	context.authId = authId
	context.imageId = imageId

	processor.exec(context)

	File(fileData.url).delete()

	return context.baseResponse(data = context.baseImage)
}