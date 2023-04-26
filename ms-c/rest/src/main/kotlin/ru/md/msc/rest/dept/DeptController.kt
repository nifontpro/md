package ru.md.msc.rest.dept

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.msc.domain.dept.biz.proc.DeptCommand
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.biz.proc.DeptProcessor
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.image.model.BaseImage
import ru.md.msc.rest.base.*
import ru.md.msc.rest.dept.mappers.fromTransport
import ru.md.msc.rest.dept.mappers.toTransportGetDeptDetails
import ru.md.msc.rest.dept.mappers.toTransportGetDepts
import ru.md.msc.rest.dept.model.request.*
import ru.md.msc.rest.dept.model.response.DeptDetailsResponse
import ru.md.msc.rest.utils.JwtUtils

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
		return process(
			processor = deptProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportGetDeptDetails() }
		)
	}

	@PostMapping("update")
	private suspend fun update(
		@RequestBody request: UpdateDeptRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<DeptDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = deptProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportGetDeptDetails() }
		)
	}

	@PostMapping("auth_subtree")
	private suspend fun getAuthSubtreeDepts(
		@RequestBody request: GetAuthSubtreeDeptsRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<List<Dept>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = deptProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportGetDepts() }
		)
	}

	@PostMapping("get_id")
	private suspend fun getDeptById(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetDeptByIdRequest
	): BaseResponse<DeptDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = deptProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportGetDeptDetails() }
		)
	}

	@PostMapping("delete")
	private suspend fun delete(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: DeleteDeptRequest
	): BaseResponse<DeptDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = deptProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportGetDeptDetails() }
		)
	}

	@PostMapping("img_add")
	suspend fun imageAdd(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestPart("file") file: MultipartFile,
		@RequestPart("authId") authId: String,
		@RequestPart("deptId") deptId: String,
	): BaseResponse<BaseImage> {
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		val context = DeptContext().apply { command = DeptCommand.IMG_ADD }
		return imageProcess(
			authData = authData,
			context = context,
			processor = deptProcessor,
			multipartFile = file,
			authId = authId.toLongOr0(),
			entityId = deptId.toLongOr0(),
		)
	}

	@PostMapping("img_update")
	suspend fun imageUpdate(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestPart("file") file: MultipartFile,
		@RequestPart("authId") authId: String,
		@RequestPart("deptId") deptId: String,
		@RequestPart("imageId") imageId: String,
	): BaseResponse<BaseImage> {
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		val context = DeptContext().apply { command = DeptCommand.IMG_UPDATE }
		return imageProcess(
			authData = authData,
			context = context,
			processor = deptProcessor,
			multipartFile = file,
			authId = authId.toLongOr0(),
			entityId = deptId.toLongOr0(),
			imageId = imageId.toLongOr0(),
		)
	}
}