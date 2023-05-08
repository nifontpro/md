package ru.md.msc.domain.user.biz.workers.sort

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.setUsersBySubdeptsValidSortedFields(title: String) = worker {
	this.title = title
	handle {
		orderFields = listOf(
			"firstname",
			"patronymic",
			"lastname",
			"authEmail",
			"post",
			"dept.name",
			"dept.classname",
		)
	}
}
