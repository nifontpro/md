package ru.md.cor.base.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.base.biz.BaseContext
import ru.md.cor.base.biz.ContextState
import ru.md.cor.worker

fun <T : BaseContext> ICorChainDsl<T>.initStatus(title: String) = worker {
	this.title = title
	on { state == ContextState.NONE }
	handle { state = ContextState.STARTING }
}
