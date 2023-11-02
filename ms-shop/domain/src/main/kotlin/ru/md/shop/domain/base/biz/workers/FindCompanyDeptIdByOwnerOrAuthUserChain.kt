package ru.md.shop.domain.base.biz.workers

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.validate.validateAuthDeptLevel
import ru.md.base_domain.dept.biz.validate.validateDeptId
import ru.md.base_domain.dept.biz.workers.getCompanyDeptIdByAuthUser
import ru.md.base_domain.dept.biz.workers.getCompanyIdByDeptId
import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.cor.worker

/**
 * Получение id компании по Владельцу (должен быть указан deptId любого отдела компании)
 * или любому другому сотруднику (id компании находится автоматически по дереву отделов)
 */
fun <T : BaseMedalsContext> ICorChainDsl<T>.findCompanyDeptIdByOwnerOrAuthUserChain() {
	chain {
		on { isAuthUserHasOwnerRole }
		validateDeptId("Проверка deptId")
		validateAuthDeptLevel("Проверка доступа к отделу")
		getCompanyIdByDeptId("Получаем deptId Компании")
		worker("") { log.info("OWNER: deptId = $deptId") }
	}
	chain {
		on { !isAuthUserHasOwnerRole }
		getCompanyDeptIdByAuthUser("Получаем deptId Компании")
		worker("") { log.info("NOT OWNER: deptId = $deptId") }
	}
}