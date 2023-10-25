package ru.md.msc.domain.base.workers.chain

import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.base.validate.auth.validateAuthDeptDownLevel
import ru.md.msc.domain.base.validate.auth.validateAuthDeptLevel
import ru.md.base_domain.biz.validate.validateAdminRole

/**
 * Проверка доступа авторизованного пользователя с правами Администратора
 * к созданию/обновлению/удалению профиля сотрудника в зависимости от его роли.
 * Администратор имеет право над сотрудниками в своем и нижестоящих отделах
 * и имеет право над Администраторами нижестоящих отделов
 */
fun <T : BaseClientContext> ICorChainDsl<T>.validateAdminModifyUserByRoleChain() {
	validateAdminRole("Проверка наличия прав Администратора")
	chain {
		on { !isModifyUserHasAdminRole } // Обновляемый без прав ADMIN
		validateAuthDeptLevel("Проверка доступа к отделу")
	}
	chain {
		on { isModifyUserHasAdminRole } // Обновляемый имеет роль ADMIN
		validateAuthDeptDownLevel("Проверка доступа к нижестоящему отделу")
	}
}