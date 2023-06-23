package ru.md.msc.domain.base.workers.image

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.image.model.ImageType
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.base.biz.s3Error

fun <T: BaseClientContext> ICorChainDsl<T>.addImageToS3(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		val imageKey = "$prefixUrl/${fileData.filename}"
		val miniKey = "$prefixUrl/mini/${fileData.filename}"

		val imageUrl = s3Repository.putObject(key = imageKey, fileUrl = fileData.imageUrl) ?: throw Exception()
		val miniUrl = s3Repository.putObject(key = miniKey, fileUrl = fileData.miniUrl) ?: throw Exception()

		baseImage = BaseImage(
			imageUrl = imageUrl,
			imageKey = imageKey,
			miniUrl = miniUrl,
			miniKey = miniKey,
			type = ImageType.USER
		)
	}

	except {
		log.error(it.message)
		s3Error()
	}
}