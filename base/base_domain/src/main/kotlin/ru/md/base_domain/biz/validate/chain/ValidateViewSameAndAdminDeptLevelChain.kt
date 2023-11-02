package ru.md.base_domain.biz.validate.chain

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.validate.validateAdminRole
import ru.md.base_domain.biz.validate.validateAuthDeptLevel
import ru.md.base_domain.dept.biz.validate.validateDeptId
import ru.md.base_domain.user.biz.validate.validateUserId
import ru.md.base_domain.user.biz.workers.getDeptIdByUserId
import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.cor.worker

/**
 * Проверка возможности просмотра своего профиля и Администратором профиля любого сотрудника
 * Для Администратора должен быть явно указан userId, для Сотрудника он берется из authId
 */
fun <T : BaseMedalsContext> ICorChainDsl<T>.validateUserIdSameOrAdminDeptLevelChain() {
	chain {
		on { isAuthUserHasAdminRole }
		chain {
			on { userId != authId }
			worker("Сохраняем deptId") { tempLong = deptId }
			validateUserId("Проверка userId")
			getDeptIdByUserId("Находим отдел для авторизации")
			validateDeptId("Проверяем deptId")
			validateAdminRole("Проверка наличия прав Администратора")
			validateAuthDeptLevel("Проверка доступа к отделу")
			worker("Восстанавливаем deptId") { deptId = tempLong }
		}

	}
	chain {
		on { !isAuthUserHasAdminRole }
		worker("") { userId = authUser.id }
	}

}