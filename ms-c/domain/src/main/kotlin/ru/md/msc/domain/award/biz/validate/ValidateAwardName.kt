package ru.md.msc.domain.award.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail

fun ICorChainDsl<AwardContext>.validateAwardName(title: String) = worker {
	this.title = title
	on { award.name.isBlank() }
	handle {
		fail(
			errorValidation(
				field = "name",
				violationCode = "blank",
				description = "Название награды не должно быть пустым"
			)
		)
	}
}
