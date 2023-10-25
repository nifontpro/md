package ru.md.msc.domain.base.workers.chain

import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.cor.worker
import ru.md.msc.domain.base.workers.findModifyUserAndGetRolesAndDeptId
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.base_domain.user.model.RoleUser

/**
 * Проверка доступа к обновлению собственного профиля Владельца, а также
 * Проверка доступа авторизованного пользователя с правами Администратора
 * к созданию/обновлению/удалению профиля сотрудника в зависимости от его роли.
 * Администратор имеет право над сотрудниками в своем и нижестоящих отделах
 * и имеет право над Администраторами нижестоящих отделов
 */

fun ICorChainDsl<UserContext>.validateSameOwnerAndAdminModifyUser() {
	chain {
		on { userId == authUser.id && authUser.roles.find { it == RoleUser.OWNER } != null }
		worker("Если Владелец, нужно автоматически удалить его отдел") { deleteDeptId = authUser.dept?.id }
	}
	chain {
		on { userId != authUser.id } // Если запрос не собственного профиля:
		findModifyUserAndGetRolesAndDeptId("Получаем профиль для обновления")
		validateAdminModifyUserByRoleChain() // Тогда должны быть права Администратора
	}
}