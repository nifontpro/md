package ru.md.msgal.domain.item.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.item.biz.proc.ItemContext

fun ICorChainDsl<ItemContext>.validateItemName(title: String) = worker {
	this.title = title
	on { item.name.isBlank() }
	handle {
		fail(
			errorValidation(
				field = "name",
				violationCode = "blank",
				description = "Название объекта не должно быть пустым"
			)
		)
	}
}
