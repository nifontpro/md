package ru.md.msgal.domain.item.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.item.biz.proc.ItemContext
import ru.md.msgal.domain.item.biz.proc.ItemNotFoundException
import ru.md.msgal.domain.item.biz.proc.getItemsError
import ru.md.msgal.domain.item.biz.proc.itemNotFoundError

fun ICorChainDsl<ItemContext>.getItemById(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		item = itemService.getById(itemId = itemId)
	}

	except {
		log.error(it.message)
		when (it) {
			is ItemNotFoundException -> itemNotFoundError()
			else -> getItemsError()
		}

	}
}