package ru.md.award.domain.medal.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.award.domain.medal.biz.proc.MedalContext

private const val MIN_SCORE = 0
private const val MAX_SCORE = 100

fun ICorChainDsl<MedalContext>.validateMedalScore(title: String) = worker {
	this.title = title
	on { medal.score < MIN_SCORE || medal.score > MAX_SCORE }
	handle {
		fail(
			errorValidation(
				field = "score",
				violationCode = "out of range",
				description = "Цена медали должна быть от $MIN_SCORE до $MAX_SCORE"
			)
		)
	}
}
