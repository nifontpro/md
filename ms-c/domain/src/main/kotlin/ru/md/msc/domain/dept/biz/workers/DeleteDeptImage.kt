package ru.md.msc.domain.dept.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.biz.ImageNotFoundException
import ru.md.msc.domain.base.biz.deleteImageError
import ru.md.msc.domain.base.biz.imageNotFoundError
import ru.md.msc.domain.dept.biz.proc.DeptContext

fun ICorChainDsl<DeptContext>.deleteDeptImage(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			baseImage = deptService.deleteImage(deptId = deptId, imageId = imageId)
		} catch (e: ImageNotFoundException) {
			imageNotFoundError()
			return@handle
		} catch (e: Exception) {
			log.info(e.message)
			deleteImageError()
		}

	}
}