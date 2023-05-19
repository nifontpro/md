package ru.md.msc.rest.base

import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_rest.emailNotVerified
import ru.md.base_rest.fileSaveError
import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.base_rest.saveFile
import ru.md.base_rest.utils.AuthData
import ru.md.msc.domain.base.biz.BaseClientContext
import java.io.File

suspend fun <C : BaseClientContext> imageProcess(
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