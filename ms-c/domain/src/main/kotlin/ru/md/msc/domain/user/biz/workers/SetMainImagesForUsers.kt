package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.setMainImagesForUsers(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		userService.updateAllUserImg()
	}

	except {
		log.error(it.message)
	}

}