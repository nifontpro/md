package ru.md.shop.domain.base.biz.validate.chain

import ru.md.base_domain.biz.validate.validateAuthDeptLevel
import ru.md.base_domain.user.biz.workers.getAuthUserAndVerifyEmail
import ru.md.cor.ICorChainDsl
import ru.md.shop.domain.base.biz.proc.ShopContext
import ru.md.shop.domain.base.biz.validate.validateProductId
import ru.md.shop.domain.base.biz.workers.findDeptIdByProductId

fun <T : ShopContext> ICorChainDsl<T>.validateProductIdAndAccessToProductChain() {
	validateProductId("Проверяем productId")
	getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
	findDeptIdByProductId("Получаем deptId")
	validateAuthDeptLevel("Проверка доступа к отделу")
}