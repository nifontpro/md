package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.base_domain.user.biz.errors.getUserError

fun ICorChainDsl<UserContext>.getUsersWithActivityByDept(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		usersAwards = userService.getUsersWithActivity(deptId = deptId, baseQuery = baseQuery)
	}

	except {
		log.error(it.message)
		getUserError()
	}

}