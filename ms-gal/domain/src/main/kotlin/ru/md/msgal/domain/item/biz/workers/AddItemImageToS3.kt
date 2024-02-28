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

		val originKey = "$prefixUrl/${imageData.imageName}"
		val originUrl = imageData.originImage?.let {
			baseS3Repository.putObjectMem(
				key = originKey,
				imageName = imageData.imageName,
				contentType = imageData.contentType,
				data = it
			)
		}

		// Поэтапное заполнение для удаления из S3 в случае ошибки
		item = item.copy(
			originUrl = originUrl,
			originKey = originKey,
			miniUrl = originUrl,
			normalUrl = originUrl,
		)

		if (imageData.compress) {
			val miniKey = "$prefixUrl/mini/${imageData.imageName}"
			val miniUrl = imageData.miniImage?.let {
				baseS3Repository.putObjectMem(
					key = miniKey,
					imageName = imageData.imageName,
					contentType = imageData.contentType,
					data = it
				)
			}

			item = item.copy(
				miniUrl = miniUrl,
				miniKey = miniKey,
			)

			if (imageData.normCompress) {
				val normalKey = "$prefixUrl/normal/${imageData.imageName}"
				val normalUrl = imageData.normalImage?.let {
					baseS3Repository.putObjectMem(
						key = normalKey,
						imageName = imageData.imageName,
						contentType = imageData.contentType,
						data = it
					)
				}

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