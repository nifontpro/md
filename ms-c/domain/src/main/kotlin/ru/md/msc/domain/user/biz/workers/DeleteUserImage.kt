package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.user.biz.proc.ImageNotFoundException
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.userImageNotFound

fun ICorChainDsl<UserContext>.deleteUserImage(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			baseImage = userService.deleteImage(userId = userId, imageId = baseImage.id ?: 0)
		} catch (e: ImageNotFoundException) {
			userImageNotFound()
		} catch (e: Exception) {
			log.info(e.message)
			fail(
				errorDb(
					repository = "user",
					violationCode = "image delete",
					description = "Ошибка удаления изображения"
				)
			)
		}
	}
}