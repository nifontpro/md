package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.getActivityError
import ru.md.msc.domain.base.biz.ContextState

fun ICorChainDsl<AwardContext>.getActiveAwardsByDept(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			activities = awardService.findActivAwardsByDept(deptId = deptId, baseQuery = baseQuery)
		} catch (e: Exception) {
			getActivityError()
		}

	}
}