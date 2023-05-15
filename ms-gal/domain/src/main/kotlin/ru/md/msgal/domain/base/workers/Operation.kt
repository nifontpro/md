package ru.md.msgal.domain.base.workers

import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.cor.worker
import ru.md.msgal.domain.base.biz.BaseGalleryContext

fun <T : BaseGalleryContext> ICorChainDsl<T>.operation(
	title: String,
	command: IBaseCommand,
	block: ICorChainDsl<T>.() -> Unit
) = chain {
	on { this.command == command && state == ContextState.STARTING }
	this.title = title
	worker("Статус - операция выполняется") { state = ContextState.RUNNING }
	block()
}