package ru.md.base_domain.biz.proc

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.image.model.FileData
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageInfo

abstract class BaseContext(
	var state: ContextState = ContextState.NONE,
	var command: IBaseCommand = BaseCommand.NONE,
	val errors: MutableList<ContextError> = mutableListOf(),

	var authEmail: String = "",
	var fileData: FileData = FileData(),
	var pageInfo: PageInfo? = null,
	var baseQuery: BaseQuery = BaseQuery(),
	var orderFields: List<String> = emptyList(), // Допустимые поля для сортировки
)

interface IBaseCommand

enum class BaseCommand : IBaseCommand {
	NONE
}

enum class ContextState {
	NONE,
	STARTING, // Старт процессора
	RUNNING,  // Старт операции
	FAILING,
	FINISHING,
}