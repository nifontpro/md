package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.biz.addImageError
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.addUserImageToDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			baseImage = userService.addImage(userId = userId, baseImage = baseImage)
		} catch (e: Exception) {
			log.error(e.message)
			deleteImageOnFailing = true
			addImageError()
		}

	}
}