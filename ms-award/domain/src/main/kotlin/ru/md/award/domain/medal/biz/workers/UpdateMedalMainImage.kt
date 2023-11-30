package ru.md.award.domain.medal.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.errors.updateMainImageError
import ru.md.award.domain.medal.biz.proc.MedalContext

fun ICorChainDsl<MedalContext>.updateMedalMainImage(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		medalImageService.setMainImage(medalId)
	}

	except {
		log.error(it.message)
		updateMainImageError()
	}

}