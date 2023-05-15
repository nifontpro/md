package ru.md.msgal.domain.base.validate

import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.base.biz.BaseGalleryContext

fun <T : BaseGalleryContext> ICorChainDsl<T>.validateFolderId(title: String) = worker {
	this.title = title
	on { folderId < 1 }
	handle {
		fail(
			errorValidation(
				field = "folderId",
				violationCode = "not valid",
				description = "Неверный folderId"
			)
		)
	}
}
