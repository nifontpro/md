package ru.md.msc.domain.base.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail

fun <T : BaseContext> ICorChainDsl<T>.validateSortedFields(title: String) = worker {
	this.title = title
	on { true }
	handle {
		orders.forEach { baseOrder ->
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
