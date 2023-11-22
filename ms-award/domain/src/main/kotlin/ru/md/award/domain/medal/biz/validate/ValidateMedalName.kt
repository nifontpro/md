package ru.md.award.domain.medal.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.award.domain.medal.biz.proc.MedalContext

fun ICorChainDsl<MedalContext>.validateMedalName(title: String) = worker {
	this.title = title
	on { medal.name.isBlank() }
	handle {
		fail(
			errorValidation(
				field = "name",
				violationCode = "blank",
				description = "Наименование медали не должно быть пустым"
			)
		)
	}
}
