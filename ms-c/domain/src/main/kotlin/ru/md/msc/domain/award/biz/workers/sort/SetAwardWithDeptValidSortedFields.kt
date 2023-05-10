package ru.md.msc.domain.award.biz.workers.sort

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext

fun ICorChainDsl<AwardContext>.setAwardWithDeptValidSortedFields(title: String) = worker {
	this.title = title
	handle {
		orderFields = listOf(
			"name",
			"type",
			"startDate",
			"endDate",
			"dept.name",
			"dept.classname",
		)
	}
}
