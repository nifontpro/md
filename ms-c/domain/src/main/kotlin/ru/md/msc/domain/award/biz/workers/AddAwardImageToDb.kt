package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail

fun ICorChainDsl<AwardContext>.addAwardImageToDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			baseImage = awardService.addImageToDb(awardId = award.id, baseImage = baseImage)
		} catch (e: Exception) {
			log.error(e.message)
			deleteBaseImageOnFailing = true
			fail(
				errorDb(
					repository = "award",
					violationCode = "image add",
					description = "Ошибка добавления изображения"
				)
			)
		}
	}
}