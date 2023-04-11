package ru.md.base.dom.workers

import ru.md.base.dom.biz.BaseContext
import ru.md.base.dom.biz.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

fun <T : BaseContext> ICorChainDsl<T>.initStatus(
	title: String = "Инициализация статуса"
) = worker {
	this.title = title
	on { state == ContextState.NONE }
	handle { state = ContextState.STARTING }
}
