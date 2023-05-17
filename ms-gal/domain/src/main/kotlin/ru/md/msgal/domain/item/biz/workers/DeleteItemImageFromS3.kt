package ru.md.msgal.domain.item.biz.workers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.item.biz.proc.ItemContext

/**
 * Асинхронное удаление объекта из s3
 */
fun ICorChainDsl<ItemContext>.deleteItemImageFromS3(title: String) = worker {
	this.title = title
	on { deleteImageOnFailing }
	handle {

		CoroutineScope(Dispatchers.IO).launch {
			try {
				s3Repository.deleteObject(key = item.imageKey)
				log.info("Gallery object ${item.imageKey} deleted")
			} catch (e: Exception) {
				log.error(e.message)
				log.error("Add ${item.imageKey} to dirty link S3")
			}
		}
	}
}