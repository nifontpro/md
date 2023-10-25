package ru.md.msc.domain.base.workers.chain

import ru.md.cor.ICorChainDsl
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.base.validate.auth.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.auth.validateAuthDeptLevel
import ru.md.base_domain.biz.validate.validateAdminRole
import ru.md.base_domain.biz.validate.validateDeptId

/**
 * Проверка возможности доступа Администратора к отделу
 */
fun <T : BaseClientContext> ICorChainDsl<T>.validateDeptIdAndAdminDeptLevelChain() {
	validateDeptId("Проверяем deptId")
	getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
	validateAdminRole("Проверка наличия прав Администратора")
	validateAuthDeptLevel("Проверка доступа к отделу")
}