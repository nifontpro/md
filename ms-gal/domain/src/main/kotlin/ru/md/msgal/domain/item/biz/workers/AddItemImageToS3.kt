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
		val prefixUrl = "F$folderId"

		val originKey = "$prefixUrl/${fileData.filename}"
		val originUrl = s3Repository.putObject(key = originKey, fileUrl = fileData.originUrl) ?: throw Exception()

		// Поэтапное заполнение для удаления из S3 в случае ошибки
		item = item.copy(
			originUrl = originUrl,
			originKey = originKey,
			miniUrl = originUrl,
			normalUrl = originUrl,
		)

		if (fileData.compress) {
			val miniKey = "$prefixUrl/mini/${fileData.filename}"
			val miniUrl = s3Repository.putObject(key = miniKey, fileUrl = fileData.miniUrl) ?: throw Exception()
			item = item.copy(
				miniUrl = miniUrl,
				miniKey = miniKey,
			)
			if (fileData.normCompress) {
				val normalKey = "$prefixUrl/normal/${fileData.filename}"
				val normalUrl = s3Repository.putObject(key = normalKey, fileUrl = fileData.normalUrl) ?: throw Exception()
				item = item.copy(
					normalUrl = normalUrl,
					normalKey = normalKey,
				)
			}
		}

	}

	except {
		log.info(it.message)
		s3Error()
	}
}