package ru.md.msgal.domain.folder.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.folder.biz.proc.FolderContext

fun ICorChainDsl<FolderContext>.trimFieldFolder(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		folder = folder.copy(
			name = folder.name.trim(),
			description = folder.description?.trim(),
		)

	}
}