package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.biz.proc.*
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.prepareUserImagePrefixUrl(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		deptId = authUser.dept?.id ?: throw DeptNotFoundException()
		rootDeptId = deptService.getRootId(deptId = deptId) ?: throw RootDeptNotFoundException()
		prefixUrl = "R$rootDeptId/D$deptId/U$userId"
	}

	except {
		log.error(it.message)
		when (it) {
			is DeptNotFoundException -> deptNotFound()
			is RootDeptNotFoundException -> rootDeptNotFound()
			else -> getDeptError()
		}
	}
}