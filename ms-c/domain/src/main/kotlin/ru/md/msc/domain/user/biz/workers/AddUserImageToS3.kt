package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.biz.s3Error
import ru.md.msc.domain.dept.biz.proc.deptNotFound
import ru.md.msc.domain.dept.biz.proc.getDeptError
import ru.md.msc.domain.image.model.BaseImage
import ru.md.msc.domain.image.model.ImageType
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.addUserImageToS3(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		val rootDeptId = try {
			deptId = authUser.dept?.id ?: run {
				deptNotFound()
				return@handle
			}
			deptService.getRootId(deptId = deptId) ?: run {
				deptNotFound()
				return@handle
			}
		} catch (e: Exception) {
			getDeptError()
		}

		try {
			val prefix = "R$rootDeptId/D$deptId/U$userId"
			val imageKey = "$prefix/${fileData.filename}"
			val imageUrl = s3Repository.putObject(key = imageKey, fileData = fileData) ?: throw Exception()
			baseImage = BaseImage(imageUrl = imageUrl, imageKey = imageKey, type = ImageType.USER)
		} catch (e: Exception) {
			log.info(e.message)
			s3Error()
		}

	}
}