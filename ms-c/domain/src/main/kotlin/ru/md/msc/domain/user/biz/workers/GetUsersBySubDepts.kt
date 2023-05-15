package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.workers.pageFun
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.getUserError

fun ICorChainDsl<UserContext>.getUsersBySubDepts(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		users = pageFun { userService.findBySubDepts(deptId = deptId, baseQuery = baseQuery) }
	}
	except {
		getUserError()
	}
}