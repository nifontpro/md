package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail

fun ICorChainDsl<AwardContext>.deleteAward(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		awardService.deleteById(awardId = awardId)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "award",
				violationCode = "delete",
				description = "Ошибка при удалении награды, возможно ею награжден сотрудник"
			)
		)
	}
}