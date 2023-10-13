package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.deleteUser(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		userService.deleteById(userId = userId, deptId = deleteDeptId)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "user",
				violationCode = "delete",
				description = "Ошибка при удалении профиля сотрудника"
			)
		)
	}

}