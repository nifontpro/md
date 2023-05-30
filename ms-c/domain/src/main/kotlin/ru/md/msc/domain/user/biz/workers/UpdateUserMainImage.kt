package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.updateUserMainImage(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		userService.setMainImage(userId = userId)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "user",
				violationCode = "update main image",
				description = "Ошибка обновления основного изображения"
			)
		)
	}

}