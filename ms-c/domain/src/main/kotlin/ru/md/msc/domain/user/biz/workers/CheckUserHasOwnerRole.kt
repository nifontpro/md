package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.checkUserHasOwnerRole(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		hasOwnerRole = userService.doesUserOwnerRole(userId = userId)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "user",
				violationCode = "get roles",
				description = "Ошибка проверки ролей Сотрудника"
			)
		)
	}

}