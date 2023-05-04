package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext

fun ICorChainDsl<AwardContext>.setActionByUserValidSortedFields(title: String) = worker {
	this.title = title
	handle {
		orderFields = listOf(
			"date",
			"actionType",
			"award.name",
			"award.type"
		)
	}
}
