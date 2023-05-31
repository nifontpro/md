package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext

fun ICorChainDsl<AwardContext>.setMainImagesForAwards(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		awardService.updateAllAwardImg()
	}

	except {
		log.error(it.message)
	}

}