package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.helper.pageFun
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.getActivityError

fun ICorChainDsl<AwardContext>.getActiveAwardsByUser(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		activities = pageFun { awardService.findActivAwardsByUser(userId = userId, baseQuery = baseQuery) }
	}

	except {
		log.error(it.message)
		getActivityError()
	}

}