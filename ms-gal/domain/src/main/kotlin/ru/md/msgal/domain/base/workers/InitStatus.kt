package ru.md.msgal.domain.base.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.base.biz.BaseGalleryContext

fun <T : BaseGalleryContext> ICorChainDsl<T>.initStatus(
	title: String = "Инициализация статуса"
) = worker {
	this.title = title
	on { state == ContextState.NONE }
	handle { state = ContextState.STARTING }
}
