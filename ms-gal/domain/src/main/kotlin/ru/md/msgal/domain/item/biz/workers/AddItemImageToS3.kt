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
			val prefix = "F$folderId"
			val imageKey = "$prefix/${fileData.filename}"
			val miniKey = "$prefix/mini/${fileData.filename}"
			val imageUrl = s3Repository.putObject(key = imageKey, fileUrl = fileData.imageUrl) ?: throw Exception()
			val miniUrl = s3Repository.putObject(key = imageKey, fileUrl = fileData.miniUrl) ?: throw Exception()
			item = item.copy(
				imageUrl = imageUrl,
				imageKey = imageKey,
				miniUrl = miniUrl,
				miniKey = miniKey,
			)
	}

	except {
		log.info(it.message)
		s3Error()
	}
}