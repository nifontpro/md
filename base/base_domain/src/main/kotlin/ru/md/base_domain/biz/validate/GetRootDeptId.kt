package ru.md.base_domain.biz.validate

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

fun <T : BaseMedalsContext> ICorChainDsl<T>.getRootDeptId(title: String) = worker {
	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		rootDeptId = baseDeptService.getRootId(deptId) ?: run {
			fail(
				errorDb(
					repository = "dept",
					violationCode = "root dept not found",
					description = "Корневой отдел не найден"
				)
			)
			return@handle
		}
	}

	except {
		fail(
			errorDb(
				repository = "dept",
				violationCode = "get root dept",
				description = "Ошибка получения корневого отдела"
			)
		)
	}
}