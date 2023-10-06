package ru.md.msc.rest.dept.mappers

import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.rest.dept.model.response.DeptDetailsResponse
import ru.md.msc.rest.dept.model.response.DeptResponse

fun DeptContext.toTransportDeptDetails(): BaseResponse<DeptDetailsResponse> {
	return baseResponse(deptDetails.toDeptDetailsResponse())
}

fun DeptContext.toTransportDeptResponse(): BaseResponse<DeptResponse> {
	return baseResponse(dept.toDeptResponse())
}

fun DeptContext.toTransportDeptsResponse(): BaseResponse<List<DeptResponse>> {
	return baseResponse(depts.map { it.toDeptResponse() })
}
