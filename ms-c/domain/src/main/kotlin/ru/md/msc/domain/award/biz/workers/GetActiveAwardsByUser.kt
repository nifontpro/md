package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.getActivityError

fun ICorChainDsl<AwardContext>.getActiveAwardsByUser(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		activities = awardService.findActivAwardsByUser(userId = userId, orders = baseQuery.orders)
	}

	except {
		log.error(it.message)
		getActivityError()
	}

}