package ru.md.award.domain.medal.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.award.domain.medal.biz.proc.MedalContext

fun ICorChainDsl<MedalContext>.deleteMedal(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		medalService.deleteById(medalId)
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "medal",
				violationCode = "delete",
				description = "Ошибка при удалении медали"
			)
		)
	}
}