package ru.md.msgal.domain.base.biz

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.md.msgal.domain.base.helper.ContextError

interface IBaseCommand

abstract class BaseContext(
	var state: ContextState = ContextState.NONE,
	var command: IBaseCommand = BaseCommand.NONE,
	val errors: MutableList<ContextError> = mutableListOf(),

	var authEmail: String = "",

//	var fileData: FileData = FileData(),
//	var baseImage: BaseImage = BaseImage(),
//	var baseImages: List<BaseImage> = emptyList(),
	var deleteImageOnFailing: Boolean = false,

//	var baseQuery: BaseQuery = BaseQuery(),
	var orderFields: List<String> = emptyList(), // Допустимые поля для сортировки

) {

	val log: Logger = LoggerFactory.getLogger(BaseContext::class.java)
}

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