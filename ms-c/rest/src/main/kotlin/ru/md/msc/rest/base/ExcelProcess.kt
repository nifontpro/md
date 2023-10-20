package ru.md.msc.rest.base

import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.base_rest.emailNotVerified
import ru.md.base_rest.fileContentTypeError
import ru.md.base_rest.fileSaveError
import ru.md.base_rest.utils.AuthData
import ru.md.msc.domain.user.biz.proc.UserCommand
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.UserProcessor
import ru.md.msc.domain.user.model.excel.AddUserReport
import java.io.File

private val mimes = listOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")

suspend fun excelProcess(
	authData: AuthData,
	processor: UserProcessor,
	multipartFile: MultipartFile,
	authId: Long,
	deptId: Long,
): BaseResponse<List<AddUserReport>> {
	val context = UserContext().apply {
		command = UserCommand.ADD_FROM_EXCEL
	}

	if (!authData.emailVerified || authData.email.isBlank()) {
		context.emailNotVerified()
		return BaseResponse.error(errors = context.errors)
	}
	context.authEmail = authData.email

	val contentType = multipartFile.contentType
	if (mimes.find { it ==contentType } == null) {
		context.fileContentTypeError(contentType ?: "", "Загружаемый файл должен быть таблицей Excel")
		return BaseResponse.error(errors = context.errors)
	}

	val fileUrl = try {
		saveExcelFile(multipartFile = multipartFile)
	} catch (e: Exception) {
		when (e) {
			is FileSaveException -> context.fileSaveError(e.message)
			else -> context.fileSaveError()
		}
		return BaseResponse.error(errors = context.errors)
	}

	context.authId = authId
	context.deptId = deptId
	context.fileUrl = fileUrl

	processor.exec(context)
	File(fileUrl).delete()
	return context.baseResponse(data = context.addReport)
}