package ru.md.msgal.domain.item.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.item.biz.proc.ItemContext

fun ICorChainDsl<ItemContext>.validateItemId(title: String) = worker {
	this.title = title
	on { itemId < 1 }
	handle {
		fail(
			errorValidation(
				field = "itemId",
				violationCode = "not valid",
				description = "Неверный itemId"
			)
		)
	}
}
