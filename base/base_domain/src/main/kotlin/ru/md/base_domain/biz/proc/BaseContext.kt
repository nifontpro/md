package ru.md.base_domain.biz.proc

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.client.MicroClient
import ru.md.base_domain.image.model.ImageData
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageInfo

abstract class BaseContext(
	var state: ContextState = ContextState.NONE,
	var command: IBaseCommand = BaseCommand.NONE,
	val errors: MutableList<ContextError> = mutableListOf(),

	var authEmail: String = "",
	var imageData: ImageData = ImageData(),
	var pageInfo: PageInfo? = null,
	var baseQuery: BaseQuery = BaseQuery(),
	var orderFields: List<String> = emptyList(), // Допустимые поля для сортировки
) {
	lateinit var microClient: MicroClient
}

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