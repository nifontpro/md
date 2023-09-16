package ru.md.msc.domain.medal.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.medal.biz.proc.MedalContext
import ru.md.msc.domain.medal.biz.proc.MedalNotFoundException
import ru.md.msc.domain.medal.biz.proc.getMedalError
import ru.md.msc.domain.medal.biz.proc.medalNotFoundError

fun ICorChainDsl<MedalContext>.getMedalByIdDetails(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		medalDetails = medalService.findMedalDetailsById(medalId = medalId)
	}

	except {
		when (it) {
			is MedalNotFoundException -> medalNotFoundError()
			else -> getMedalError()
		}
	}

}