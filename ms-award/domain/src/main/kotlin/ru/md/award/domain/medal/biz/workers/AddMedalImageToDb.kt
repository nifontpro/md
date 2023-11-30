package ru.md.award.domain.medal.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.errors.addImageError
import ru.md.award.domain.medal.biz.proc.MedalContext

fun ICorChainDsl<MedalContext>.addMedalImageToDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		baseImage = medalImageService.addImage(medalId = medalId, baseImage = baseImage)
	}

	except {
		log.error(it.message)
		deleteImageOnFailing = true
		addImageError()
	}

}