package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.base_domain.user.biz.errors.getUserError

fun ICorChainDsl<UserContext>.getDeptIdByUserId(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		deptId = userService.findDeptIdByUserId(userId = userId)
	}

	except {
		log.error(it.message)
		getUserError()
	}

}