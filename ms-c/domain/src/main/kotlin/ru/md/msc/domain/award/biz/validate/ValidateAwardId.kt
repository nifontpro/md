package ru.md.msc.domain.award.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail

fun ICorChainDsl<AwardContext>.validateAwardId(title: String) = worker {
	this.title = title
	on { award.id < 1 }
	handle {
		fail(
			errorValidation(
				field = "id",
				violationCode = "not valid",
				description = "Неверный id награды"
			)
		)
	}
}
