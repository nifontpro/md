package ru.md.award.domain.medal.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.errors.ImageNotFoundException
import ru.md.base_domain.errors.deleteImageError
import ru.md.base_domain.errors.imageNotFoundError
import ru.md.award.domain.medal.biz.proc.MedalContext

fun ICorChainDsl<MedalContext>.deleteMedalImageFromDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		baseImage = medalService.deleteImage(medalId = medalId, imageId = imageId)
	}

	except {
		log.info(it.message)
		when (it) {
			is ImageNotFoundException -> imageNotFoundError()
			else -> deleteImageError()
		}
	}
}