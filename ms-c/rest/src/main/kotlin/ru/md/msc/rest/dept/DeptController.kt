package ru.md.msc.rest.dept

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.model.BaseResponse
import ru.md.base_rest.base.authProcess
import ru.md.base_rest.base.toLongOr0
import ru.md.base_rest.image.baseImageProcess
import ru.md.base_rest.model.mapper.toTransportBaseImageResponse
import ru.md.base_rest.model.request.AUTH
import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.base_rest.utils.JwtUtils
import ru.md.msc.domain.dept.biz.proc.DeptCommand
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.biz.proc.DeptProcessor
import ru.md.msc.rest.dept.mappers.fromTransport
import ru.md.msc.rest.dept.mappers.toTransportDeptDetails
import ru.md.msc.rest.dept.mappers.toTransportDeptResponse
import ru.md.msc.rest.dept.mappers.toTransportDeptsResponse
import ru.md.msc.rest.dept.model.request.*
import ru.md.msc.rest.dept.model.response.DeptDetailsResponse
import ru.md.msc.rest.dept.model.response.DeptResponse

@RestController
@RequestMapping("dept")
class DeptController(
	private val deptProcessor: DeptProcessor,
	private val jwtUtils: JwtUtils,
) {

	@PostMapping("create")
	private suspend fun create(
		@RequestBody request: CreateDeptRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<DeptDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptDetails() }
		)
	}

	@PostMapping("update")
	private suspend fun update(
		@RequestBody request: UpdateDeptRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<DeptDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptDetails() }
		)
	}

	@PostMapping("auth_subtree")
	private suspend fun getAuthSubtreeDepts(
		@RequestBody request: GetAuthSubtreeDeptsRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<List<DeptResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptsResponse() }
		)
	}

	@PostMapping("top_level_tree")
	private suspend fun getAuthTopLevelTreeDepts(
		@RequestBody request: GetAuthTopLevelTreeDeptsRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<List<DeptResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptsResponse() }
		)
	}

	@PostMapping("get_id")
	private suspend fun getDeptByIdDetails(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetDeptByIdDetailsRequest
	): BaseResponse<DeptDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptDetails() }
		)
	}

	@PostMapping("get_id_m")
	private suspend fun getDeptById(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetDeptByIdRequest
	): BaseResponse<DeptResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptResponse() }
		)
	}

	@PostMapping("get_auth_dept")
	private suspend fun getAuthDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetAuthDeptRequest
	): BaseResponse<DeptResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptResponse() }
		)
	}

	/**
	 * Получение списка отделов по parentId
	 */
	@PostMapping("current_list")
	private suspend fun getCurrentDepts(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetCurrentDeptsRequest
	): BaseResponse<List<DeptResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptsResponse() }
		)
	}

	@PostMapping("delete")
	private suspend fun delete(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: DeleteDeptRequest
	): BaseResponse<DeptDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptDetails() }
		)
	}

	@PostMapping("img_add")
	suspend fun imageAdd(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestPart("file") file: MultipartFile,
		@RequestPart("authId") authId: String,
		@RequestPart("deptId") deptId: String,
	): BaseResponse<BaseImageResponse> {
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		val context = DeptContext().apply { command = DeptCommand.IMG_ADD }
		return baseImageProcess(
			authData = authData,
			context = context,
			processor = deptProcessor,
			multipartFile = file,
			authId = authId.toLongOr0(),
			entityId = deptId.toLongOr0(),
		)
	}

	@PostMapping("img_delete")
	private suspend fun imageDelete(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: DeleteDeptImageRequest
	): BaseResponse<BaseImageResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseImageResponse() }
		)
	}

	/**
	 * !!!! Set ADMIN role
	 */
//	@PostMapping("admin/img")
//	private suspend fun setMainImages(
//		@RequestHeader(name = AUTH) bearerToken: String,
//		@RequestBody request: SetMainDeptImagesRequest
//	): BaseResponse<Unit> {
//		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
//		return authProcess(
//			processor = deptProcessor,
//			authRequest = baseRequest,
//			fromTransport = { fromTransport(it) },
//			toTransport = { toTransportUnit() }
//		)
//	}

}