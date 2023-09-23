package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.user.biz.proc.UserNotFoundException
import ru.md.msc.domain.user.biz.proc.getUserError
import ru.md.msc.domain.user.biz.proc.userNotFoundError

fun ICorChainDsl<AwardContext>.findUserDeptIdByUserId(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		userDeptId = userService.findDeptIdByUserId(userId = userId)
	}

	except {
		log.error(it.message)
		when (it) {
			is UserNotFoundException -> userNotFoundError()
			else -> getUserError()
		}
	}
}