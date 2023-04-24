package ru.md.msc.rest.dept

import org.springframework.web.bind.annotation.*
import ru.md.msc.domain.dept.biz.proc.DeptProcessor
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.rest.base.AUTH
import ru.md.msc.rest.base.BaseResponse
import ru.md.msc.rest.base.process
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
}