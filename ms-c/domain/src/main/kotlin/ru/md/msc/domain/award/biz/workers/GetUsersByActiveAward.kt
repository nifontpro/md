package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.getActivityError
import ru.md.msc.domain.base.biz.ContextState

fun ICorChainDsl<AwardContext>.getUsersByActiveAward(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			activities = awardService.findUsersByActivAward(awardId = awardId, orders = baseQuery.orders)
		} catch (e: Exception) {
			getActivityError()
		}

	}
}