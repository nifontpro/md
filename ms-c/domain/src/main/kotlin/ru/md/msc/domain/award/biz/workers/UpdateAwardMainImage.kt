package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.base_domain.errors.updateMainImageError

fun ICorChainDsl<AwardContext>.updateAwardMainImage(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		awardService.setMainImage(awardId = awardId)
	}

	except {
		log.error(it.message)
		updateMainImageError()
	}

}