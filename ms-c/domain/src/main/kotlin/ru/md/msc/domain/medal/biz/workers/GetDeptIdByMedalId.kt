package ru.md.msc.domain.medal.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.medal.biz.proc.MedalContext
import ru.md.msc.domain.medal.biz.proc.MedalNotFoundException
import ru.md.msc.domain.medal.biz.proc.medalNotFoundError

fun ICorChainDsl<MedalContext>.getDeptIdByMedalId(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		deptId = medalService.findDeptIdByMedalId(medalId)
	}

	except {
		when (it) {
			is MedalNotFoundException -> medalNotFoundError()
			else -> fail(
				errorDb(
					repository = "medal",
					violationCode = "get deptId error",
					description = "Ошибка получения id отдела медали"
				)
			)
		}
	}
}