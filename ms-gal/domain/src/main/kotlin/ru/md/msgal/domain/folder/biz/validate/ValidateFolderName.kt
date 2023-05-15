package ru.md.msgal.domain.folder.biz.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.folder.biz.proc.FolderContext

fun ICorChainDsl<FolderContext>.validateFolderName(title: String) = worker {
	this.title = title
	on { folder.name.isBlank() }
	handle {
		fail(
			errorValidation(
				field = "name",
				violationCode = "blank",
				description = "Название папки не должно быть пустым"
			)
		)
	}
}
