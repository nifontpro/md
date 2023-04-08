package ru.md.base.domain.biz.workers

import ru.md.base.domain.biz.BaseContext
import ru.md.base.domain.biz.ContextState
import ru.otus.cor.ICorChainDsl
import ru.otus.cor.worker

fun <T : BaseContext> ICorChainDsl<T>.initStatus(title: String) = worker {
	this.title = title
	on { state == ContextState.NONE }
	handle { state = ContextState.STARTING }
}
