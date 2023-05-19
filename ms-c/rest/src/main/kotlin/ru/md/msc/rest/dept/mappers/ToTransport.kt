package ru.md.msc.rest.dept.mappers

import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.model.Dept
import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.msc.rest.dept.model.response.DeptDetailsResponse

fun DeptContext.toTransportDeptDetails(): BaseResponse<DeptDetailsResponse> {
	return baseResponse(deptDetails.toDeptDetailsResponse())
}

fun DeptContext.toTransportDepts(): BaseResponse<List<Dept>> {
	return baseResponse(depts)
}
