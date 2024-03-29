package ru.md.msgal.rest.folder

import org.springframework.web.bind.annotation.*
import ru.md.base_rest.model.request.AUTH
import ru.md.base_domain.model.BaseResponse
import ru.md.base_rest.base.authProcess
import ru.md.base_rest.utils.JwtUtils
import ru.md.msgal.domain.folder.biz.proc.FolderProcessor
import ru.md.msgal.rest.folder.mappers.fromTransport
import ru.md.msgal.rest.folder.mappers.toTransportFolder
import ru.md.msgal.rest.folder.mappers.toTransportFolders
import ru.md.msgal.rest.folder.model.request.CreateFolderRequest
import ru.md.msgal.rest.folder.model.request.DeleteFolderRequest
import ru.md.msgal.rest.folder.model.request.GetAllFolderRequest
import ru.md.msgal.rest.folder.model.request.UpdateFolderRequest
import ru.md.msgal.rest.folder.model.response.FolderResponse

@RestController
@RequestMapping("folder")
class FolderController(
	private val folderProcessor: FolderProcessor,
	private val jwtUtils: JwtUtils,
) {

	@PostMapping("admin/create")
	private suspend fun create(
		@RequestBody request: CreateFolderRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<FolderResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = folderProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportFolder() }
		)
	}

	@PostMapping("admin/update")
	private suspend fun update(
		@RequestBody request: UpdateFolderRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<FolderResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = folderProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportFolder() }
		)
	}

	@PostMapping("admin/delete")
	private suspend fun delete(
		@RequestBody request: DeleteFolderRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<FolderResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = folderProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportFolder() }
		)
	}

	/**
	 * Получить все дерево папок галереи
	 * baseRequest:
	 *    Допустимые поля для сортировки:
	 *          "parentId",
	 *          "name",
	 *          "createdAt",
	 */
	@PostMapping("get_all")
	private suspend fun getAll(
		@RequestBody request: GetAllFolderRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<List<FolderResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = folderProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportFolders() }
		)
	}
}