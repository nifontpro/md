package ru.md.base.dom.biz

import ru.md.base.dom.helper.ContextError

interface IBaseCommand

abstract class BaseContext(
	var state: ContextState = ContextState.NONE,
	var command: IBaseCommand,
	val errors: MutableList<ContextError> = mutableListOf(),
)

@Suppress("unused")
enum class ContextState {
	NONE,
	STARTING, // Старт процессора
	RUNNING,  // Старт операции
	FAILING,
	FINISHING,
}