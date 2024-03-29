package ru.md.award.domain.medal.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.award.domain.medal.biz.proc.MedalContext
import ru.md.award.domain.medal.biz.proc.MedalNotFoundException
import ru.md.award.domain.medal.biz.proc.getMedalError
import ru.md.award.domain.medal.biz.proc.medalNotFoundError

fun ICorChainDsl<MedalContext>.getMedalByIdDetails(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		medalDetails = medalService.findMedalDetailsById(medalId = medalId)
	}

	except {
		log.error(it.message)
		when (it) {
			is MedalNotFoundException -> medalNotFoundError()
			else -> getMedalError()
		}
	}

}