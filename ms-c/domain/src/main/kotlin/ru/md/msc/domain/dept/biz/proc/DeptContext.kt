package ru.md.msc.domain.dept.biz.proc

import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.IBaseCommand
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails

class DeptContext : BaseContext() {
	var dept: Dept = Dept()
	var deptDetails: DeptDetails = DeptDetails()
	var depts: List<Dept> = emptyList()
	var deptsDetails: List<DeptDetails> = emptyList()
}

enum class DeptCommand : IBaseCommand {
	CREATE,
	GET_DEPTS_TREE
}
