package ru.md.msc.rest.dept

import org.springframework.web.bind.annotation.*
import ru.md.msc.domain.dept.biz.proc.DeptProcessor
import ru.md.msc.rest.base.AUTH
import ru.md.msc.rest.base.BaseResponse
import ru.md.msc.rest.base.process
import ru.md.msc.rest.dept.mappers.fromTransport
import ru.md.msc.rest.dept.mappers.toTransportGetDeptDetails
import ru.md.msc.rest.dept.model.request.CreateDeptRequest
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
}