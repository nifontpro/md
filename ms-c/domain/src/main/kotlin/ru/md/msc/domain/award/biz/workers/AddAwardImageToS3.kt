package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.biz.s3Error

fun ICorChainDsl<AwardContext>.addAwardImageToS3(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			baseImage = awardService.addImageToS3(awardId = award.id, fileData = fileData)
		} catch (e: Exception) {
			log.error(e.message)
			s3Error()
		}
	}
}