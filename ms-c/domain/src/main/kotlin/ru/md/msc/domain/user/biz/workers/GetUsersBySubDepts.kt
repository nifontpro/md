package ru.md.msc.domain.user.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.biz.MustPageableException
import ru.md.msc.domain.base.biz.mustPageableError
import ru.md.msc.domain.base.workers.pageFun
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.getUserError

fun ICorChainDsl<UserContext>.getUsersBySubDepts(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		try {
			users = pageFun { userService.findBySubDepts(deptId = deptId, baseQuery = baseQuery) }
		} catch (e: MustPageableException) {
			mustPageableError()
		} catch (e: Exception) {
			getUserError()
		}
	}
}