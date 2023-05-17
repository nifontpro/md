package ru.md.msgal.domain.item.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.base.biz.s3Error
import ru.md.msgal.domain.item.biz.proc.ItemContext

fun ICorChainDsl<ItemContext>.addItemImageToS3(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			val prefix = "F$folderId"
			val imageKey = "$prefix/${fileData.filename}"
			val imageUrl = s3Repository.putObject(key = imageKey, fileData = fileData) ?: throw Exception()
//			baseImage = BaseImage(imageUrl = imageUrl, imageKey = imageKey, type = ImageType.USER)
			item = item.copy(
				imageUrl = imageUrl,
				imageKey = imageKey
			)
		} catch (e: Exception) {
			log.info(e.message)
			s3Error()
		}
	}
}