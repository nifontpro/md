package ru.md.msc.rest.dept

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_rest.imageProcess
import ru.md.base_rest.model.AUTH
import ru.md.base_rest.model.BaseResponse
import ru.md.base_rest.process
import ru.md.base_rest.toLongOr0
import ru.md.msc.domain.dept.biz.proc.DeptCommand
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.biz.proc.DeptProcessor
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.rest.base.*
import ru.md.msc.rest.base.mappers.toTransportBaseImage
import ru.md.msc.rest.dept.mappers.fromTransport
import ru.md.msc.rest.dept.mappers.toTransportDeptDetails
import ru.md.msc.rest.dept.mappers.toTransportDepts
import ru.md.msc.rest.dept.model.request.*
import ru.md.msc.rest.dept.model.response.DeptDetailsResponse
import ru.md.base_rest.utils.JwtUtils

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
		return process(
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
	): BaseResponse<List<Dept>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDepts() }
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
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptDetails() }
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

	@PostMapping("img_delete")
	private suspend fun imageDelete(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: DeleteDeptImageRequest
	): BaseResponse<BaseImage> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseImage() }
		)
	}

}