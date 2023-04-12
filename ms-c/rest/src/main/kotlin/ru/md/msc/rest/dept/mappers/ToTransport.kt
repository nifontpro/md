package ru.md.msc.rest.dept.mappers

import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.rest.base.BaseResponse
import ru.md.msc.rest.base.baseResponse
import ru.md.msc.rest.dept.model.response.DeptDetailsResponse

fun DeptContext.toTransportGetDeptDetails(): BaseResponse<DeptDetailsResponse> {
	return baseResponse(deptDetails.toDeptDetailsResponse())
}
