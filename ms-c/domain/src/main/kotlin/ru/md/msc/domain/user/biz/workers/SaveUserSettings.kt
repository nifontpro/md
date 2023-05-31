package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.saveUserSettings(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		userSettings = userService.saveSettings(userSettings = userSettings)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "user",
				violationCode = "save settings",
				description = "Ошибка сохранения настроек"
			)
		)
	}

}