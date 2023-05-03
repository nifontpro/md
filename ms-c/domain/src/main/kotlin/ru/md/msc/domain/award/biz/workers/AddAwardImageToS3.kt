package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.biz.s3Error
import ru.md.msc.domain.dept.biz.proc.deptNotFound
import ru.md.msc.domain.dept.biz.proc.getDeptError
import ru.md.msc.domain.image.model.BaseImage
import ru.md.msc.domain.image.model.ImageType

fun ICorChainDsl<AwardContext>.addAwardImageToS3(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		val rootDeptId = try {
			deptService.getRootId(deptId = deptId) ?: run {
				deptNotFound()
				return@handle
			}
		} catch (e: Exception) {
			getDeptError()
			return@handle
		}

		try {
			val prefix = "R$rootDeptId/D$deptId/A$awardId"
			val imageKey = "$prefix/${fileData.filename}"
			val imageUrl = s3Repository.putObject(key = imageKey, fileData = fileData) ?: throw Exception()
			baseImage = BaseImage(imageUrl = imageUrl, imageKey = imageKey, type = ImageType.USER)
		} catch (e: Exception) {
			log.info(e.message)
			s3Error()
		}
	}
}