package ru.md.msc.domain.dept.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.biz.addImageError
import ru.md.msc.domain.dept.biz.proc.DeptContext

fun ICorChainDsl<DeptContext>.addDeptImageToDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			baseImage = deptService.addImage(deptId = deptId, baseImage = baseImage)
		} catch (e: Exception) {
			log.info(e.message)
			deleteImageOnFailing = true
			addImageError()
		}
	}
}