package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.dept.biz.proc.RootDeptNotFoundException
import ru.md.msc.domain.dept.biz.proc.getDeptError
import ru.md.msc.domain.dept.biz.proc.rootDeptNotFound

fun ICorChainDsl<AwardContext>.prepareAwardImagePrefixUrl(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		rootDeptId = deptService.getRootId(deptId = deptId) ?: throw RootDeptNotFoundException()
		prefixUrl = "R$rootDeptId/D$deptId/A$awardId"
	}

	except {
		log.error(it.message)
		when (it) {
			is RootDeptNotFoundException -> rootDeptNotFound()
			else -> getDeptError()
		}
	}
}