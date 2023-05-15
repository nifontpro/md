@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package ru.md.msgal.rest.folder

import org.springframework.web.bind.annotation.*
import ru.md.base_rest.model.AUTH
import ru.md.base_rest.model.BaseResponse
import ru.md.base_rest.process
import ru.md.base_rest.utils.JwtUtils
import ru.md.msgal.domain.folder.biz.proc.FolderProcessor
import ru.md.msgal.rest.folder.mappers.fromTransport
import ru.md.msgal.rest.folder.mappers.toTransportFolder
import ru.md.msgal.rest.folder.model.request.CreateFolderRequest
import ru.md.msgal.rest.folder.model.response.FolderResponse

@RestController
@RequestMapping("folder")
class FolderController(
	private val folderProcessor: FolderProcessor,
	private val jwtUtils: JwtUtils,
) {

	@PostMapping("create")
	private suspend fun create(
		@RequestBody request: CreateFolderRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<FolderResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = folderProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportFolder() }
		)
	}
}