package ru.md.msc.rest.dept.mappers

import ru.md.msc.domain.dept.biz.proc.DeptCommand
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.rest.dept.model.request.CreateDeptRequest

fun DeptContext.fromTransport(request: CreateDeptRequest) {
	command = DeptCommand.CREATE
	authId = request.authId
	dept = Dept(
		parentId = request.parentId,
		name = request.name,
		classname = request.classname
	)
	deptDetails = DeptDetails(
		dept = dept,
		address = request.address,
		email = request.email,
		phone = request.phone,
		description = request.description
	)
}