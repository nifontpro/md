package ru.md.base_domain.image.biz.workers

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

/**
 * Асинхронное удаление объекта из s3
 */
fun <T : BaseMedalsContext> ICorChainDsl<T>.deleteBaseImageFromS3(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING || deleteImageOnFailing }
	handle {
			try {
				baseS3Repository.deleteBaseImage(baseImage)
			} catch (e: Exception) {
				log.error(e.message)
				log.error("Add ${baseImage.normalKey} to dirty link S3")
			}
	}
}