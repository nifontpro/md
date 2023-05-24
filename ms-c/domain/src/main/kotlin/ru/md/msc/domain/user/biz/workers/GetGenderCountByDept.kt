package ru.md.msc.domain.user.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.getUserCountError

fun ICorChainDsl<UserContext>.getGenderCountByDept(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		genderCount = userService.getGenderCountByDept(deptId = deptId, subdepts = baseQuery.subdepts)
	}

	except {
		log.error(it.message)
		getUserCountError()
	}

}