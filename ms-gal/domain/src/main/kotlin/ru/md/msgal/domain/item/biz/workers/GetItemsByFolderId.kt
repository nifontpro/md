package ru.md.msgal.domain.item.biz.workers

import ru.md.base_domain.biz.helper.pageFun
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.item.biz.proc.ItemContext
import ru.md.msgal.domain.item.biz.proc.getItemsError

fun ICorChainDsl<ItemContext>.getItemsByFolderId(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		items = pageFun { itemService.getByFolderId(folderId = folderId, baseQuery = baseQuery) }
	}

	except {
		getItemsError()
	}
}