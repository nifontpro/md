package ru.md.msgal.domain.item.biz.workers

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
			try {
				s3Repository.deleteObject(key = item.imageKey)
				log.info("Gallery object ${item.imageKey} deleted")
				s3Repository.deleteObject(key = item.miniKey)
				log.info("Gallery object ${item.miniKey} deleted")
			} catch (e: Exception) {
				log.error(e.message)
				log.error("Add ${item.imageKey} to dirty link S3")
			}
	}
}