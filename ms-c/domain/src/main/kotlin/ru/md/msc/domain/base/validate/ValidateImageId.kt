package ru.md.msc.domain.base.validate

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.helper.errorValidation
import ru.md.msc.domain.base.helper.fail

fun <T : BaseContext> ICorChainDsl<T>.validateImageId(title: String) = worker {
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
