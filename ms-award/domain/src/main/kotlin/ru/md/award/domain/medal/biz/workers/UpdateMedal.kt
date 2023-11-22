package ru.md.award.domain.medal.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.award.domain.medal.biz.proc.MedalContext

fun ICorChainDsl<MedalContext>.updateMedal(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		medalDetails = medalService.update(medalDetails)
	}

	except {
		fail(
			errorDb(
				repository = "medal",
				violationCode = "update",
				description = "Ошибка обновления медали"
			)
		)
	}
}