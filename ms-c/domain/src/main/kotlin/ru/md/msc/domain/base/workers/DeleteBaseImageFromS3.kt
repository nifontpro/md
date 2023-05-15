package ru.md.msc.domain.base.workers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext

/**
 * Асинхронное удаление объекта из s3
 */
fun <T : BaseClientContext> ICorChainDsl<T>.deleteBaseImageFromS3(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING || deleteImageOnFailing }
	handle {

		CoroutineScope(Dispatchers.IO).launch {
			try {
				s3Repository.deleteBaseImage(baseImage)
				log.info("Object ${baseImage.imageKey} deleted")
			} catch (e: Exception) {
				log.error(e.message)
				log.error("Add ${baseImage.imageKey} to dirty link S3")
			}
		}
	}
}