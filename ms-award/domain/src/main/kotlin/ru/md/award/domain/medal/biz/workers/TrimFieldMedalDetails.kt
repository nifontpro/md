package ru.md.award.domain.medal.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.award.domain.medal.biz.proc.MedalContext
import java.time.LocalDateTime

fun ICorChainDsl<MedalContext>.trimFieldMedalDetails(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		medal = medal.copy(
			name = medal.name.trim(),
		)

		medalDetails = medalDetails.copy(
			medal = medal,
			description = medalDetails.description?.trim(),
			createdAt = LocalDateTime.now()
		)
	}
}