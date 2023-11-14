package ru.md.msc.domain.dept.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.biz.proc.DeptContext

fun ICorChainDsl<DeptContext>.saveDeptSettings(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		deptSettings = deptSettingsService.saveSettings(deptSettings.copy(deptId = deptId))
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "dept",
				violationCode = "save settings",
				description = "Ошибка сохранения настроек отдела"
			)
		)
	}
}