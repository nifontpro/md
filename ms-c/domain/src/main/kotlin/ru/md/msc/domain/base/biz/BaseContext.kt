package ru.md.msc.domain.base.biz

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.md.msc.domain.base.helper.ContextError
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.service.UserService

interface IBaseCommand

abstract class BaseContext(
	var state: ContextState = ContextState.NONE,
	var command: IBaseCommand = BaseCommand.NONE,
	val errors: MutableList<ContextError> = mutableListOf(),

	var authEmail: String = "",
	var authId: Long = 0,
	var authDeptId: Long = 0,
	var authUser: User = User()
) {
	lateinit var userService: UserService
	lateinit var deptService: DeptService

	val log: Logger = LoggerFactory.getLogger(BaseContext::class.java)
}

enum class BaseCommand : IBaseCommand {
	NONE
}

@Suppress("unused")
enum class ContextState {
	NONE,
	STARTING, // Старт процессора
	RUNNING,  // Старт операции
	FAILING,
	FINISHING,
}