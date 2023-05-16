package ru.md.msgal.domain.folder.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.folder.biz.proc.FolderContext
import ru.md.msgal.domain.folder.biz.proc.FolderNotFoundException
import ru.md.msgal.domain.folder.biz.proc.folderNotFoundError

fun ICorChainDsl<FolderContext>.deleteFolder(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		folder = folderService.delete(folderId = folderId)
	}

	except {
		log.error(it.message)
		when (it) {
			is FolderNotFoundException -> folderNotFoundError()
			else -> fail(
				errorDb(
					repository = "folder",
					violationCode = "delete",
					description = "Ошибка удаления папки (возможно не пустая)"
				)
			)
		}
	}
}