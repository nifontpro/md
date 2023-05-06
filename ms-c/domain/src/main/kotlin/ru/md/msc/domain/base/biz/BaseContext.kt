package ru.md.msc.domain.base.biz

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.md.msc.domain.base.helper.ContextError
import ru.md.msc.domain.base.model.BaseQuery
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.image.model.BaseImage
import ru.md.msc.domain.image.model.FileData
import ru.md.msc.domain.image.repository.S3Repository
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.service.UserService

interface IBaseCommand

abstract class BaseContext(
	var state: ContextState = ContextState.NONE,
	var command: IBaseCommand = BaseCommand.NONE,
	val errors: MutableList<ContextError> = mutableListOf(),

	var authEmail: String = "",
	var authId: Long = 0,
	var authUser: User = User(),
	var isAuthUserHasAdminRole: Boolean = false,

	var userId: Long = 0,
	var deptId: Long = 0,
	var imageId: Long = 0,

	var fileData: FileData = FileData(),
	var baseImage: BaseImage = BaseImage(),
	var baseImages: List<BaseImage> = emptyList(),
	var deleteImageOnFailing: Boolean = false,

	var baseQuery: BaseQuery = BaseQuery(),
	var orderFields: List<String> = emptyList(), // Допустимые поля для сортировки

	) {
	lateinit var userService: UserService
	lateinit var deptService: DeptService
	lateinit var s3Repository: S3Repository

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