package ru.md.msc.domain.medal.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.dept.biz.errors.RootDeptNotFoundException
import ru.md.base_domain.dept.biz.errors.getDeptError
import ru.md.base_domain.dept.biz.errors.rootDeptNotFound
import ru.md.msc.domain.medal.biz.proc.MedalContext

fun ICorChainDsl<MedalContext>.prepareMedalImagePrefixUrl(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		rootDeptId = baseDeptService.getRootId(deptId = deptId) ?: throw RootDeptNotFoundException()
		prefixUrl = "R$rootDeptId/D$deptId/M$medalId"
	}

	except {
		log.error(it.message)
		when (it) {
			is RootDeptNotFoundException -> rootDeptNotFound()
			else -> getDeptError()
		}
	}
}