package ru.md.msc.domain.award.biz.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.AwardType
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail
import java.time.LocalDateTime

fun ICorChainDsl<AwardContext>.validateAwardPeriod(title: String) = worker {
	this.title = title
	on {
		val now = LocalDateTime.now()
		award.type == AwardType.PERIOD && (award.startDate > now || award.endDate < now)
	}
	handle {
		fail(
			errorValidation(
				field = "period",
				violationCode = "not valid",
				description = "Невозможно выполнить действие вне периода награды"
			)
		)
	}
}
