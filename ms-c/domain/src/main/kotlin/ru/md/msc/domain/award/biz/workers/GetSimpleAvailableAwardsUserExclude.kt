package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.helper.pageFun
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.getAwardError

fun ICorChainDsl<AwardContext>.getSimpleAvailableAwardsUserExclude(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		awards = pageFun {
			awardService.findSimpleAwardUserAvailable(
				deptId = authUser.dept?.id ?: 0,
				userId = userId,
				baseQuery = baseQuery
			)
		}
	}

	except {
		getAwardError()
	}

}