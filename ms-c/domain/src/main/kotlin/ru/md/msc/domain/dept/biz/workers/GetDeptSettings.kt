package ru.md.msc.domain.dept.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.model.DeptSettings

fun ICorChainDsl<DeptContext>.getDeptSettings(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		deptSettings = deptSettingsService.getSettings(deptId) ?: DeptSettings()
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "dept",
				violationCode = "get settings",
				description = "Ошибка получения настроек отдела"
			)
		)
	}
}