package ru.md.base.dom.workers

import ru.md.base.dom.biz.BaseContext
import ru.md.base.dom.biz.ContextState
import ru.md.base.dom.biz.IBaseCommand
import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.cor.worker

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