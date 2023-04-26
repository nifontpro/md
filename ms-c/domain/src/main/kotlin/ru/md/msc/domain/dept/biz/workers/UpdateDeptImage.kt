package ru.md.msc.domain.dept.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.biz.imageNotFoundError
import ru.md.msc.domain.base.biz.updateImageError
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.user.biz.proc.ImageNotFoundException

fun ICorChainDsl<DeptContext>.updateDeptImage(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			baseImage = deptService.updateImage(deptId = deptId, imageId = imageId, fileData = fileData)
		} catch (e: ImageNotFoundException) {
			imageNotFoundError()
			return@handle
		} catch (e: Exception) {
			log.info(e.message)
			updateImageError()
		}

	}
}