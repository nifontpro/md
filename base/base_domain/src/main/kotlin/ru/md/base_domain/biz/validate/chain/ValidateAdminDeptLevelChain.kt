package ru.md.base_domain.biz.validate.chain

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.cor.ICorChainDsl
import ru.md.base_domain.user.biz.workers.getAuthUserAndVerifyEmail
import ru.md.base_domain.biz.validate.validateAuthDeptLevel
import ru.md.base_domain.biz.validate.validateAdminRole
import ru.md.base_domain.dept.biz.validate.validateDeptId

/**
 * Проверка возможности доступа Администратора к отделу
 */
fun <T : BaseMedalsContext> ICorChainDsl<T>.validateDeptIdAndAdminDeptLevelChain() {
	validateDeptId("Проверяем deptId")
	getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
	validateAdminRole("Проверка наличия прав Администратора")
	validateAuthDeptLevel("Проверка доступа к отделу")
}