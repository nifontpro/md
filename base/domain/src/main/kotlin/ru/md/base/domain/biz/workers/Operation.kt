package ru.md.base.domain.biz.workers

import ru.md.base.domain.biz.BaseContext
import ru.md.base.domain.biz.ContextState
import ru.md.base.domain.biz.IBaseCommand
import ru.otus.cor.ICorChainDsl
import ru.otus.cor.chain
import ru.otus.cor.worker

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