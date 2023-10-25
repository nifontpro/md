package ru.md.msc.domain.base.workers.image

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.image.model.ImageType
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.base.biz.s3Error

fun <T : BaseClientContext> ICorChainDsl<T>.addImageToS3(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		val originKey = "$prefixUrl/${fileData.filename}"
		val originUrl = baseS3Repository.putObject(key = originKey, fileUrl = fileData.originUrl) ?: throw Exception()

		// Поэтапное заполнение для удаления из S3 в случае ошибки
		baseImage = BaseImage(
			originUrl = originUrl,
			originKey = originKey,
			miniUrl = originUrl,
			normalUrl = originUrl,
			type = ImageType.USER
		)

		if (fileData.compress) {
			val miniKey = "$prefixUrl/mini/${fileData.filename}"
			val miniUrl = baseS3Repository.putObject(key = miniKey, fileUrl = fileData.miniUrl) ?: throw Exception()
			baseImage = baseImage.copy(
				miniUrl = miniUrl,
				miniKey = miniKey,
			)
			if (fileData.normCompress) {
				val normalKey = "$prefixUrl/normal/${fileData.filename}"
				val normalUrl = baseS3Repository.putObject(key = normalKey, fileUrl = fileData.normalUrl) ?: throw Exception()
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