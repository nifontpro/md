package ru.md.msc.domain.dept.biz.proc

import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.IBaseCommand
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails

class DeptContext : BaseContext() {
	var dept: Dept = Dept()
	var deptDetails: DeptDetails = DeptDetails()
	val depts: MutableList<Dept> = mutableListOf()
	val deptsDetails: MutableList<DeptDetails> = mutableListOf()
	var emailVerified: Boolean = false
}

enum class DeptCommand : IBaseCommand {
	CREATE,
}
