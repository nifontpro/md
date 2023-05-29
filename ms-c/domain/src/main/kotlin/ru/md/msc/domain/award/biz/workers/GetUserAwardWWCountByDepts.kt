package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.getAwardCountError

fun ICorChainDsl<AwardContext>.getUserAwardWWCountByDepts(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		wwAwardCount = awardService.getWWAwardCount(deptId = deptId, baseQuery = baseQuery)
	}

	except {
		getAwardCountError()
	}

}