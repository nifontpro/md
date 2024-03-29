package ru.md.award.domain.medal.biz.validate

import ru.md.award.domain.medal.biz.proc.MedalContext
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

fun ICorChainDsl<MedalContext>.validateMedalId(title: String) = worker {
	this.title = title
	on { medalId < 1 }
	handle {
		fail(
			errorValidation(
				field = "medalId",
				violationCode = "not valid",
				description = "Неверный id медали"
			)
		)
	}
}
