package ru.md.base.dom.workers

import ru.md.base.dom.biz.BaseContext
import ru.md.base.dom.biz.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker

fun <T : BaseContext> ICorChainDsl<T>.finishOperation(
	title: String = "Завершение операции"
) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {
		state = ContextState.FINISHING
	}
}
