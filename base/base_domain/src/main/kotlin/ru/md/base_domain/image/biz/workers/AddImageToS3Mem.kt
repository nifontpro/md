package ru.md.base_domain.image.biz.workers

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.errors.s3Error
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.image.model.ImageType
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

fun <T : BaseMedalsContext> ICorChainDsl<T>.addImageToS3(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

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
		baseImage = BaseImage(
			originUrl = originUrl,
			originKey = originKey,
			miniUrl = originUrl,
			normalUrl = originUrl,
			type = ImageType.USER
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

			baseImage = baseImage.copy(
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

				baseImage = baseImage.copy(
					normalUrl = normalUrl,
					normalKey = normalKey,
				)
			}
		}
	}

	except {
		log.error(it.message)
		s3Error()
	}
}