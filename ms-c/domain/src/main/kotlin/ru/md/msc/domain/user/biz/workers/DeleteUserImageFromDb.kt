package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.errors.ImageNotFoundException
import ru.md.base_domain.errors.deleteImageError
import ru.md.base_domain.errors.imageNotFoundError
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.deleteUserImageFromDb(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		baseImage = userService.deleteImage(userId = userId, imageId = imageId)
	}

	except {
		log.error(it.message)
		when (it) {
			is ImageNotFoundException -> imageNotFoundError()
			else -> deleteImageError()
		}
	}

}