package ru.md.base_domain.dept.biz.workers

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.dept.biz.errors.CompanyDeptNotFoundException
import ru.md.base_domain.dept.biz.errors.companyDeptNotFoundError
import ru.md.base_domain.dept.biz.errors.getDeptError
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

fun <T : BaseMedalsContext> ICorChainDsl<T>.getCompanyDeptIdByAuthUser(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		deptId = baseDeptService.getCompanyDeptId(deptId = authUser.dept?.id ?: 0)
	}

	except {
		log.error(it.message)
		when (it) {
			is CompanyDeptNotFoundException -> companyDeptNotFoundError()
			else -> getDeptError()
		}
	}

}