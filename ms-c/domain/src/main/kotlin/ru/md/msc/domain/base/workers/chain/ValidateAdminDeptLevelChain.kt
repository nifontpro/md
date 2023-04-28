package ru.md.msc.domain.base.workers.chain

import ru.md.cor.ICorChainDsl
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.validate.db.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.db.validateAuthDeptLevel
import ru.md.msc.domain.base.validate.validateAdminRole
import ru.md.msc.domain.base.validate.validateDeptId

/**
 * Проверка возможности доступа Администратора к отделу
 */
fun <T : BaseContext> ICorChainDsl<T>.validateAdminDeptLevelChain() {
	validateDeptId("Проверяем deptId")
	getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
	validateAdminRole("Проверка наличия прав Администратора")
	validateAuthDeptLevel("Проверка доступа к отделу")
}