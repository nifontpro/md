package ru.md.base_domain.user.biz.workers

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.user.biz.errors.getUserError
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

fun <T : BaseMedalsContext> ICorChainDsl<T>.getDeptIdByUserId(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		deptId = baseUserService.findDeptIdByUserId(userId = userId)
	}

	except {
		log.error(it.message)
		getUserError()
	}

}