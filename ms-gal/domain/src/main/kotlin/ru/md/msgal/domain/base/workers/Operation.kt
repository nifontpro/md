package ru.md.msgal.domain.base.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.cor.worker
import ru.md.msgal.domain.base.biz.BaseContext
import ru.md.msgal.domain.base.biz.ContextState
import ru.md.msgal.domain.base.biz.IBaseCommand

fun <T : BaseContext> ICorChainDsl<T>.operation(
	title: String,
	command: IBaseCommand,
	block: ICorChainDsl<T>.() -> Unit
) = chain {
	on { this.command == command && state == ContextState.STARTING }
	this.title = title
	worker("Статус - операция выполняется") { state = ContextState.RUNNING }
	block()
}