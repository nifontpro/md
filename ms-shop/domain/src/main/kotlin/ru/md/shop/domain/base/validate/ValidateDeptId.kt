package ru.md.shop.domain.base.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.base.biz.BaseShopContext

fun <T : BaseShopContext> ICorChainDsl<T>.validateDeptId(title: String) = worker {
	this.title = title
	on { deptId < 1 }
	handle {
		fail(
			errorValidation(
				field = "deptId",
				violationCode = "not valid",
				description = "Неверный deptId"
			)
		)
	}
}
