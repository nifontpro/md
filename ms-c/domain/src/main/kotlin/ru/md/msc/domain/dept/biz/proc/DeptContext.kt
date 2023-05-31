package ru.md.msc.domain.dept.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails

class DeptContext : BaseClientContext() {
	var dept: Dept = Dept()
	var deptDetails: DeptDetails = DeptDetails()
	var depts: List<Dept> = emptyList()

	//	var deptsDetails: List<DeptDetails> = emptyList()
	var addTestUser: Boolean = false
}

enum class DeptCommand : IBaseCommand {
	CREATE,
	GET_DEPTS_TREE,
	GET_DEPT_BY_ID_DETAILS,
	DELETE,
	UPDATE,
	IMG_ADD,
	IMG_DELETE,
	SET_MAIN_IMG,
}
