package ru.md.msc.rest.base

import org.springframework.web.multipart.MultipartFile
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.IBaseProcessor
import ru.md.msc.rest.utils.AuthData
import java.io.File

suspend fun <C : BaseContext> imageProcess(
	authData: AuthData,
	context: C,
	processor: IBaseProcessor<C>,
	multipartFile: MultipartFile,
	entityId: Long,
	description: String?,
): BaseResponse<Unit> {
	if (!authData.emailVerified || authData.email.isBlank()) {
		context.emailNotVerified()
		return context.baseResponse(Unit)
	}
	context.authEmail = authData.email

	val fileData = saveFile(multipartFile = multipartFile, entityId = entityId, description = description) ?: run {
		context.fileSaveError()
		return context.baseResponse(Unit)
	}
	context.fileData = fileData

	processor.exec(context)

	File(fileData.url).delete()
	return context.baseResponse(Unit)
}