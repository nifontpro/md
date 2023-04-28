package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.biz.ImageNotFoundException
import ru.md.msc.domain.base.biz.deleteImageError
import ru.md.msc.domain.base.biz.imageNotFoundError

fun ICorChainDsl<AwardContext>.deleteAwardImageFromDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			baseImage = awardService.deleteImage(awardId = award.id, imageId = imageId)
		} catch (e: ImageNotFoundException) {
			imageNotFoundError()
		} catch (e: Exception) {
			log.info(e.message)
			deleteImageError()
		}
	}
}