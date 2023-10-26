package ru.md.base_domain.image.biz.workers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

fun <T : BaseMedalsContext> ICorChainDsl<T>.deleteBaseImagesFromS3(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {

		/**
		 * Удаление в фоновом режиме, не задерживая основной процесс
		 */
		CoroutineScope(Dispatchers.IO).launch {
			baseImages.forEach {
				try {
					baseS3Repository.deleteBaseImage(it)
					log.info("Object ${it.normalKey} deleted")
				} catch (e: Exception) {
					log.error("Add ${it.normalKey} to dirty link S3")
				}
			}
			log.info("All Images deleted on ${System.currentTimeMillis()}")
		}

		log.info("Exit block deleteBaseImagesFromS3 on ${System.currentTimeMillis()}")
	}
}