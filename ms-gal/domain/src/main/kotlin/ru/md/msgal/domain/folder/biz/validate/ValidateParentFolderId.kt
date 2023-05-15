package ru.md.msgal.domain.folder.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.folder.biz.proc.FolderContext

fun ICorChainDsl<FolderContext>.validateParentFolderId(title: String) = worker {
	this.title = title
	on { folder.parentId < 1 }
	handle {
		fail(
			errorValidation(
				field = "parentId",
				violationCode = "not valid",
				description = "Неверный parentId"
			)
		)
	}
}
