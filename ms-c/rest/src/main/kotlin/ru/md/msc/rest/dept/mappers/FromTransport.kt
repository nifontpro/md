package ru.md.msc.rest.dept.mappers

import ru.md.msc.domain.dept.biz.proc.DeptCommand
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.dept.model.DeptType
import ru.md.msc.rest.dept.model.request.CreateDeptRequest
import ru.md.msc.rest.dept.model.request.DeleteDeptRequest
import ru.md.msc.rest.dept.model.request.GetAuthSubtreeDeptsRequest
import ru.md.msc.rest.dept.model.request.GetDeptByIdRequest

fun DeptContext.fromTransport(request: CreateDeptRequest) {
	command = DeptCommand.CREATE
	authId = request.authId
	dept = Dept(
		parentId = request.parentId,
		name = request.name,
		classname = request.classname,
		type = DeptType.SIMPLE
	)
	deptDetails = DeptDetails(
		dept = dept,
		address = request.address,
		email = request.email,
		phone = request.phone,
		description = request.description
	)

	addTestUser = request.addTestUser
}

fun DeptContext.fromTransport(request: GetAuthSubtreeDeptsRequest) {
	command = DeptCommand.GET_DEPTS_TREE
	authId = request.authId
}

fun DeptContext.fromTransport(request: GetDeptByIdRequest) {
	command = DeptCommand.GET_DEPT_BY_ID_DETAILS
	authId = request.authId
	deptId = request.deptId
}

fun DeptContext.fromTransport(request: DeleteDeptRequest) {
	command = DeptCommand.DELETE
	authId = request.authId
	deptId = request.deptId
}