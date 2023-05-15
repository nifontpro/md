package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.AwardNotFoundException
import ru.md.msc.domain.award.biz.proc.awardNotFoundError
import ru.md.msc.domain.award.biz.proc.getAwardError

fun ICorChainDsl<AwardContext>.findDeptIdByAwardId(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			deptId = awardService.findDeptIdByAwardId(awardId = awardId)
		} catch (e: AwardNotFoundException) {
			awardNotFoundError()
		} catch (e: Exception) {
			getAwardError()
		}

	}
}