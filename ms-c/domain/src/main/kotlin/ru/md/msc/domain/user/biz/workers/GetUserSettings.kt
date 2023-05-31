package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.UserSettings

fun ICorChainDsl<UserContext>.getUserSettings(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		userSettings = userService.getSettings(userId = userId) ?: UserSettings(notFound = true)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "user",
				violationCode = "get settings",
				description = "Ошибка чтения настроек"
			)
		)
	}

}