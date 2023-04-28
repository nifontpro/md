package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.biz.addImageError
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.UserNotFoundException
import ru.md.msc.domain.user.biz.proc.userNotFoundError

fun ICorChainDsl<UserContext>.addUserImage(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			baseImage = userService.addImage(userId = userId, fileData = fileData)
		} catch (e: UserNotFoundException) {
			userNotFoundError()
		} catch (e: Exception) {
			log.info(e.message)
			addImageError()
		}

	}
}