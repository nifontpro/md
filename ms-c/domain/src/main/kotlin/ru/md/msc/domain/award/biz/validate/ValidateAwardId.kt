package ru.md.msc.domain.award.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail

fun ICorChainDsl<AwardContext>.validateAwardId(title: String) = worker {
	this.title = title
	on { awardId < 1 }
	handle {
		fail(
			errorValidation(
				field = "awardId",
				violationCode = "not valid",
				description = "Неверный id награды"
			)
		)
	}
}
