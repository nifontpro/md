package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.AwardNotFoundException
import ru.md.msc.domain.award.biz.proc.awardNotFoundError
import ru.md.msc.domain.award.biz.proc.getAwardError
import ru.md.msc.domain.base.biz.ContextState

fun ICorChainDsl<AwardContext>.getAwardById(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		award = awardService.findByIdLazy(awardId = awardId)
	}

	except {
		log.error(it.message)
		when (it) {
			is AwardNotFoundException -> awardNotFoundError()
			else -> getAwardError()
		}
	}
}