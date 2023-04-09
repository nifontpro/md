package ru.md.cor.base.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.base.biz.BaseContext
import ru.md.cor.base.biz.ContextState
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
