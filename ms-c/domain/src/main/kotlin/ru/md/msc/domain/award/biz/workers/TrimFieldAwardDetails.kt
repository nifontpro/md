package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.AwardType

fun ICorChainDsl<AwardContext>.trimFieldAwardDetails(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		award = award.copy(
			name = award.name.trim(),
			endDate = if (award.type == AwardType.SIMPLE) award.startDate else award.endDate,
		)

		awardDetails = awardDetails.copy(
			award = award,
			description = awardDetails.description?.trim(),
			criteria = awardDetails.criteria?.trim()
		)
	}
}