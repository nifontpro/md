package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.getActivityError
import ru.md.base_domain.biz.helper.pageFun

fun ICorChainDsl<AwardContext>.getActiveAwardsByDept(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		activities = pageFun {
			awardService.findActivAwardsByDept(
				deptId = deptId,
				awardState = awardState,
				baseQuery = baseQuery
			)
		}
	}

	except {
		getActivityError()
	}
}