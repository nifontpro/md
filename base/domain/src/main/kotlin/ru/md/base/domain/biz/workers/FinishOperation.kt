package ru.md.base.domain.biz.workers

import ru.md.base.domain.biz.BaseContext
import ru.md.base.domain.biz.ContextState
import ru.otus.cor.ICorChainDsl
import ru.otus.cor.worker

fun <T : BaseContext> ICorChainDsl<T>.finishOperation(
	title: String = "Завершение операции"
) = worker {
	this.title = title
	on { state == ContextState.RUNNING }
	handle {
		state = ContextState.FINISHING
	}
}
