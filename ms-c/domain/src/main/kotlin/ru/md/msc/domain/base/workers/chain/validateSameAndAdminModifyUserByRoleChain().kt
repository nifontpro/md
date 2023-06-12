package ru.md.msc.domain.base.workers.chain

import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.base.workers.findModifyUserAndGetRolesAndDeptId

/**
 * Проверка доступа к обновлению собственного профиля, а также
 * Проверка доступа авторизованного пользователя с правами Администратора
 * к созданию/обновлению/удалению профиля сотрудника в зависимости от его роли.
 * Администратор имеет право над сотрудниками в своем и нижестоящих отделах
 * и имеет право над Администраторами нижестоящих отделов
 */

fun <T : BaseClientContext> ICorChainDsl<T>.validateSameAndAdminModifyUser() {
	chain {
		on { userId != authUser.id } // Если запрос не собственного профиля:
		findModifyUserAndGetRolesAndDeptId("Получаем профиль для обновления")
		validateAdminModifyUserByRoleChain() // Тогда должны быть права Администратора
	}
}