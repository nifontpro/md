package ru.md.msc.domain.dept.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.dept.model.DeptSettings

class DeptContext : BaseClientContext() {
	var dept: Dept = Dept()
	var deptDetails: DeptDetails = DeptDetails()
	var depts: List<Dept> = emptyList()

	var deptSettings: DeptSettings = DeptSettings()


	//	var deptsDetails: List<DeptDetails> = emptyList()
	var addTestUser: Boolean = false
}

enum class DeptCommand : IBaseCommand {
	CREATE,
	GET_AUTH_SUB_TREE,
	GET_AUTH_DEPT,
	GET_TOP_LEVEL_TREE,
	GET_CURRENT_DEPTS,
	GET_DEPT_BY_ID,
	GET_DEPT_BY_ID_DETAILS,
	DELETE,
	UPDATE,
	IMG_ADD,
	IMG_DELETE,
	SAVE_SETTINGS,
	GET_SETTINGS,
	SET_MAIN_IMG,
}
