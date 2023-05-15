package ru.md.msc.domain.award.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.AwardType
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail

fun ICorChainDsl<AwardContext>.validateAwardDates(title: String) = worker {
	this.title = title
	on { award.type == AwardType.PERIOD && award.startDate > award.endDate }
	handle {
		fail(
			errorValidation(
				field = "date",
				violationCode = "not valid",
				description = "Дата начала номинации не может быть больше даты ее окончания"
			)
		)
	}
}
