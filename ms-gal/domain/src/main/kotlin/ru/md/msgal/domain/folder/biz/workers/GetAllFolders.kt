package ru.md.msgal.domain.folder.biz.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.folder.biz.proc.FolderContext
import ru.md.msgal.domain.folder.biz.proc.getFolderError

fun ICorChainDsl<FolderContext>.getAllFolders(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		folders = folderService.getAll(orders = baseQuery.orders)
	}

	except {
		log.error(it.message)
		getFolderError()
	}
}