package ru.md.msgal.domain.folder.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.folder.biz.proc.FolderContext

fun ICorChainDsl<FolderContext>.createFolder(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		folder = folderService.create(folder)
	}

	except {
		fail(
			errorDb(
				repository = "folder",
				violationCode = "create",
				description = "Ошибка создания папки галереи"
			)
		)
	}
}