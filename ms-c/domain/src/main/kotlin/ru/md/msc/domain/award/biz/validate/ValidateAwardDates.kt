package ru.md.msc.domain.award.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail

fun ICorChainDsl<AwardContext>.validateAwardDates(title: String) = worker {
	this.title = title
	on { award.startDate > award.endDate }
	handle {
		fail(
			errorValidation(
				field = "date",
				violationCode = "not valid",
				description = "Дата начала не может быть больше дата окончания"
			)
		)
	}
}
