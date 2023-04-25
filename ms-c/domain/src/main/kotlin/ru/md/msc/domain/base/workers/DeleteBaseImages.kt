package ru.md.msc.domain.base.workers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState

fun <T : BaseContext> ICorChainDsl<T>.deleteBaseImagesFromS3(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		CoroutineScope(Dispatchers.IO).launch {
			baseImages.forEach {
				try {
					s3Repository.deleteBaseImage(it)
					log.info("Object ${it.imageKey} deleted")
				} catch (e: Exception) {
					log.error("Add ${it.imageKey} to dirty link S3")
				}
			}
			log.info("All Images deleted on ${System.currentTimeMillis()}")
		}

		log.info("Exit block deleteBaseImagesFromS3 on ${System.currentTimeMillis()}")
	}
}