package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.errors.addImageError
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.addUserImageToDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		baseImage = userService.addImage(userId = userId, baseImage = baseImage)
	}

	except {
		log.error(it.message)
		deleteImageOnFailing = true
		addImageError()
	}

}