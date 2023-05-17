package ru.md.base_domain.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.BaseContext
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

fun <T : BaseContext> ICorChainDsl<T>.validateSortedFields(title: String) = worker {
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
