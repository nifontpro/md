package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail

fun ICorChainDsl<AwardContext>.updateAward(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		awardDetails = awardService.update(awardDetails = awardDetails)
	}

	except {
		fail(
			errorDb(
				repository = "award",
				violationCode = "update",
				description = "Ошибка обновления награды"
			)
		)
	}
}