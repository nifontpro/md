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
		item.normalKey?.let {
			baseS3Repository.deleteObject(key = it)
			log.info("Gallery object $it deleted")
		}
		item.miniKey?.let {
			baseS3Repository.deleteObject(key = it)
			log.info("Gallery object $it deleted")
		}
		item.originKey?.let {
			baseS3Repository.deleteObject(key = it)
			log.info("Gallery object $it deleted")
		}
	}

	except {
		log.error(it.message)
	}
}