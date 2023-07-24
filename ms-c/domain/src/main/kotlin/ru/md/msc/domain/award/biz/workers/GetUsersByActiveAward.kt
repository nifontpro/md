package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.helper.pageFun
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.getActivityError
import ru.md.msc.domain.award.model.ActionType

fun ICorChainDsl<AwardContext>.getUsersByActiveAward(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		val actionTypeNull = if (actionType == ActionType.UNDEF) null else actionType
		activities = pageFun {
			awardService.findUsersByActivAward(
				awardId = awardId,
				actionType = actionTypeNull,
				baseQuery = baseQuery
			)
		}
	}

	except {
		log.error(it.message)
		getActivityError()
	}

}