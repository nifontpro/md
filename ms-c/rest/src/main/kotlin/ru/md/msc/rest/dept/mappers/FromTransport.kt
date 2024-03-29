package ru.md.msc.rest.dept.mappers

import ru.md.base_domain.dept.model.Dept
import ru.md.base_domain.dept.model.DeptType
import ru.md.base_rest.model.mapper.toBaseQuery
import ru.md.msc.domain.dept.biz.proc.DeptCommand
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.dept.model.DeptSettings
import ru.md.msc.rest.dept.model.request.*

fun DeptContext.fromTransport(request: CreateDeptRequest) {
	command = DeptCommand.CREATE
	authId = request.authId
	dept = Dept(
		parentId = request.parentId,
		name = request.name,
		classname = request.classname,
		topLevel = request.topLevel,
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

fun DeptContext.fromTransport(request: UpdateDeptRequest) {
	command = DeptCommand.UPDATE
	authId = request.authId
	deptId = request.deptId
	dept = Dept(
		id = request.deptId,
		name = request.name,
		classname = request.classname,
		topLevel = request.topLevel
	)
	deptDetails = DeptDetails(
		dept = dept,
		address = request.address,
		email = request.email,
		phone = request.phone,
		description = request.description
	)
}

fun DeptContext.fromTransport(request: GetAuthSubtreeDeptsRequest) {
	command = DeptCommand.GET_AUTH_SUB_TREE
	authId = request.authId
	baseQuery = request.baseRequest.toBaseQuery()
}

fun DeptContext.fromTransport(request: GetAuthDeptRequest) {
	command = DeptCommand.GET_AUTH_DEPT
	authId = request.authId
}

fun DeptContext.fromTransport(request: GetAuthTopLevelTreeDeptsRequest) {
	command = DeptCommand.GET_TOP_LEVEL_TREE
	authId = request.authId
	baseQuery = request.baseRequest.toBaseQuery()
}

fun DeptContext.fromTransport(request: GetDeptByIdDetailsRequest) {
	command = DeptCommand.GET_DEPT_BY_ID_DETAILS
	authId = request.authId
	deptId = request.deptId
}

fun DeptContext.fromTransport(request: GetDeptByIdRequest) {
	command = DeptCommand.GET_DEPT_BY_ID
	authId = request.authId
	deptId = request.deptId
}

fun DeptContext.fromTransport(request: GetCurrentDeptsRequest) {
	command = DeptCommand.GET_CURRENT_DEPTS
	authId = request.authId
	deptId = request.parentId
	baseQuery = request.baseRequest.toBaseQuery()
}

fun DeptContext.fromTransport(request: DeleteDeptRequest) {
	command = DeptCommand.DELETE
	authId = request.authId
	deptId = request.deptId
}

fun DeptContext.fromTransport(request: DeleteDeptImageRequest) {
	command = DeptCommand.IMG_DELETE
	authId = request.authId
	deptId = request.deptId
	imageId = request.imageId
}

fun DeptContext.fromTransport(request: SaveDeptSettingsRequest) {
	command = DeptCommand.SAVE_SETTINGS
	authId = request.authId
	deptId = request.deptId
	deptSettings = DeptSettings(payName = request.payName)
}

fun DeptContext.fromTransport(request: GetDeptSettingsRequest) {
	command = DeptCommand.GET_SETTINGS
	authId = request.authId
	deptId = request.deptId
}

@Suppress("UNUSED_PARAMETER")
fun DeptContext.fromTransport(request: SetMainDeptImagesRequest) {
	command = DeptCommand.SET_MAIN_IMG
}