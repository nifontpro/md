package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.biz.ImageNotFoundException
import ru.md.msc.domain.base.biz.deleteImageError
import ru.md.msc.domain.base.biz.imageNotFoundError
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.deleteUserImageFromDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			baseImage = userService.deleteImage(userId = userId, imageId = imageId)
		} catch (e: ImageNotFoundException) {
			imageNotFoundError()
		} catch (e: Exception) {
			log.info(e.message)
			deleteImageError()
		}
	}
}