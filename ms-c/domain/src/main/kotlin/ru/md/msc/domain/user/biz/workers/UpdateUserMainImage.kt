package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.errors.updateMainImageError
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.updateUserMainImage(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		userService.setMainImage(userId = userId)
	}

	except {
		log.error(it.message)
		updateMainImageError()
	}

}