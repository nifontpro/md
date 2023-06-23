package ru.md.msc.domain.dept.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.biz.proc.RootDeptNotFoundException
import ru.md.msc.domain.dept.biz.proc.getDeptError
import ru.md.msc.domain.dept.biz.proc.rootDeptNotFound

fun ICorChainDsl<DeptContext>.prepareDeptImagePrefixUrl(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		rootDeptId = deptService.getRootId(deptId = deptId) ?: throw RootDeptNotFoundException()
		prefixUrl = "R$rootDeptId/D$deptId/IMAGES"
	}

	except {
		log.error(it.message)
		when (it) {
			is RootDeptNotFoundException -> rootDeptNotFound()
			else -> getDeptError()
		}
	}
}