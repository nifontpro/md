package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.getAwardError
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.workers.pageFun

fun ICorChainDsl<AwardContext>.getAvailableAwardsBySubdepts(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		awards = pageFun { awardService.findBySubDept(deptId = authUser.dept?.id ?: 0, baseQuery = baseQuery) }
	}

	except {
		getAwardError()
	}

}