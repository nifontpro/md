package ru.md.msc.rest.user.excel

import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.model.BaseResponse
import ru.md.base_domain.model.baseResponse
import ru.md.base_rest.base.emailNotVerified
import ru.md.base_rest.base.fileContentTypeError
import ru.md.base_rest.base.fileSaveError
import ru.md.base_rest.utils.AuthData
import ru.md.msc.domain.user.biz.proc.UserCommand
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.UserProcessor
import ru.md.msc.domain.user.model.excel.AddUserReport
import ru.md.msc.domain.user.model.excel.UpdateKey
import java.io.File

private val mimes = listOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")

suspend fun excelProcess(
	authData: AuthData,
	processor: UserProcessor,
	multipartFile: MultipartFile,
	authId: Long,
	deptId: Long,
	updateKey: UpdateKey,
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
	context.updateKey = updateKey

	context.log.info("--> updateKey: $updateKey")

	processor.exec(context)
	File(fileUrl).delete()
	return context.baseResponse(data = context.addReport)
}