package ru.md.msc.domain.base.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail

fun <T : BaseClientContext> ICorChainDsl<T>.validateImageId(title: String) = worker {
	this.title = title
	on { imageId < 1 }
	handle {
		fail(
			errorValidation(
				field = "imageId",
				violationCode = "not valid",
				description = "Неверный imageId"
			)
		)
	}
}
