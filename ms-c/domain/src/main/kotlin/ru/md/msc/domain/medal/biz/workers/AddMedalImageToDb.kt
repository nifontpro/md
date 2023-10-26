package ru.md.msc.domain.medal.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.errors.addImageError
import ru.md.msc.domain.medal.biz.proc.MedalContext

fun ICorChainDsl<MedalContext>.addMedalImageToDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		baseImage = medalService.addImage(medalId = medalId, baseImage = baseImage)
	}

	except {
		log.error(it.message)
		deleteImageOnFailing = true
		addImageError()
	}

}