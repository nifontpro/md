package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.image.model.ImageType
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.s3Error
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.addUserImageToS3(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		val imageKey = "$prefixUrl/${fileData.filename}"
		val imageUrl = s3Repository.putObject(key = imageKey, fileData = fileData) ?: throw Exception()
		baseImage = BaseImage(imageUrl = imageUrl, imageKey = imageKey, type = ImageType.USER)
	}

	except {
		log.error(it.message)
		s3Error()
	}
}