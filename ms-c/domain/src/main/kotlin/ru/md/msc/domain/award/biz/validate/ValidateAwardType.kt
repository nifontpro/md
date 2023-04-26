package ru.md.msc.domain.award.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.AwardType
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail

fun ICorChainDsl<AwardContext>.validateAwardType(title: String) = worker {
	this.title = title
	on { award.type == AwardType.UNDEF }
	handle {
		fail(
			errorValidation(
				field = "type",
				violationCode = "undef",
				description = "Тип награды должен быть определен"
			)
		)
	}
}
