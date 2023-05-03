package ru.md.msc.domain.award.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.ActionType
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail

fun ICorChainDsl<AwardContext>.validateActionType(title: String) = worker {
	this.title = title
	on { actionType == ActionType.UNDEF }
	handle {
		fail(
			errorValidation(
				field = "actionType",
				violationCode = "not valid",
				description = "Недопустимый тип действия с наградой"
			)
		)
	}
}
