package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext

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