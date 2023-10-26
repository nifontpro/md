package ru.md.msc.domain.dept.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.base_domain.dept.biz.errors.RootDeptNotFoundException
import ru.md.base_domain.dept.biz.errors.getDeptError
import ru.md.base_domain.dept.biz.errors.rootDeptNotFound

fun ICorChainDsl<DeptContext>.prepareDeptImagePrefixUrl(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		rootDeptId = baseDeptService.getRootId(deptId = deptId) ?: throw RootDeptNotFoundException()
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