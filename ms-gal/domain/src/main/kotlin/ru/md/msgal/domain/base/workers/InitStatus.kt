package ru.md.msgal.domain.base.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msgal.domain.base.biz.BaseContext
import ru.md.msgal.domain.base.biz.ContextState

fun <T : BaseContext> ICorChainDsl<T>.initStatus(
	title: String = "Инициализация статуса"
) = worker {
	this.title = title
	on { state == ContextState.NONE }
	handle { state = ContextState.STARTING }
}
