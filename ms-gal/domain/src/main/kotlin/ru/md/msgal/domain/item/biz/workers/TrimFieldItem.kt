package ru.md.msgal.domain.item.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.item.biz.proc.ItemContext

fun ICorChainDsl<ItemContext>.trimFieldItem(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		item = item.copy(
			name = item.name.trim(),
			description = item.description?.trim(),
		)

	}
}