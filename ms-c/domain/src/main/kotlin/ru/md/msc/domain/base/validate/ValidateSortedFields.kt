package ru.md.msc.domain.base.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail

fun <T : BaseClientContext> ICorChainDsl<T>.validateSortedFields(title: String) = worker {
	this.title = title
	on { true }
	handle {
		baseQuery.orders.forEach { baseOrder ->
			val field = baseOrder.field
			val fieldNotExist = orderFields.find { field == it } == null
			if (fieldNotExist) {
				fail(
					errorValidation(
						field = field,
						violationCode = "not sorted",
						description = "Недопустимое поле для сортировки: $field"
					)
				)
			}
		}
	}
}
