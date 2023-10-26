package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.base_domain.errors.ImageNotFoundException
import ru.md.base_domain.errors.deleteImageError
import ru.md.base_domain.errors.imageNotFoundError

fun ICorChainDsl<AwardContext>.deleteAwardImageFromDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		baseImage = awardService.deleteImage(awardId = awardId, imageId = imageId)
	}

	except {
		log.info(it.message)
		when (it) {
			is ImageNotFoundException -> imageNotFoundError()
			else -> deleteImageError()
		}
	}
}