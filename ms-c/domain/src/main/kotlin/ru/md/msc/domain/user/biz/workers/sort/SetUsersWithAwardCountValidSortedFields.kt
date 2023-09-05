package ru.md.msc.domain.user.biz.workers.sort

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.setUsersWithAwardCountValidSortedFields(title: String) = worker {
	this.title = title
	handle {
		orderFields = listOf(
			"firstname",
			"patronymic",
			"lastname",
			"post",
			"(scores)",
			"(awardCount)",
			"(deptName)",
			"(classname)",
		)
	}
}
