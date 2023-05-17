package ru.md.msgal.domain.base.validate

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.base.biz.BaseGalleryContext

fun <T : BaseGalleryContext> ICorChainDsl<T>.validateFolderExist(title: String) = worker {
	this.title = title
	on { !folderService.doesFolderExist(folderId = folderId) }
	handle {
		fail(
			errorDb(
				repository = "folder",
				violationCode = "not exist",
				description = "Папка с заданным id не существует"
			)
		)
	}
}
