package ru.md.msc.domain.dept.biz.proc

import ru.md.base.dom.biz.BaseContext
import ru.md.base.dom.biz.IBaseCommand
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.dept.service.DeptService

class DeptContext : BaseContext(command = DeptCommand.NONE) {
	var dept: Dept = Dept()
	var userDetails: DeptDetails = DeptDetails()
	val users: MutableList<Dept> = mutableListOf()
	val usersDetails: MutableList<DeptDetails> = mutableListOf()
	var emailVerified: Boolean = false

	lateinit var deptService: DeptService
}

enum class DeptCommand : IBaseCommand {
	NONE,
	CREATE,
}
