package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext

fun ICorChainDsl<AwardContext>.setActionByAwardValidSortedFields(title: String) = worker {
	this.title = title
	handle {
		orderFields = listOf(
			"date",
			"actionType",
			"user.firstname",
			"user.lastname",
			"user.patronymic",
			"user.post",
		)
	}
}
