package ru.md.base_domain.biz.proc

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.image.model.FileData
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageInfo

abstract class BaseContext(
	var state: ContextState = ContextState.NONE,
	var command: IBaseCommand = BaseCommand.NONE,
	val errors: MutableList<ContextError> = mutableListOf(),

	var authId: Long = 0,
	var authEmail: String = "",
	var fileData: FileData = FileData(),
	var pageInfo: PageInfo? = null,
	var baseQuery: BaseQuery = BaseQuery(),
	var orderFields: List<String> = emptyList(), // Допустимые поля для сортировки

	var imageId: Long = 0,
	var baseImage: BaseImage = BaseImage(),
	var baseImages: List<BaseImage> = emptyList(),
	var deleteImageOnFailing: Boolean = false,
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