package ru.md.msc.rest.base

import org.springframework.web.multipart.MultipartFile
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.IBaseProcessor
import ru.md.msc.domain.image.model.BaseImage
import ru.md.msc.rest.utils.AuthData
import java.io.File

suspend fun <C : BaseContext> imageProcess(
	authData: AuthData,
	context: C,
	processor: IBaseProcessor<C>,
	multipartFile: MultipartFile,
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
	context.imageId = imageId

	processor.exec(context)

	File(fileData.url).delete()

	return context.baseResponse(data = context.baseImage)
}