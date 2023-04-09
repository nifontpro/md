package ru.md.msc.domain.user.biz.proc

import ru.md.cor.base.biz.BaseContext
import ru.md.cor.base.biz.IBaseCommand
import ru.md.msc.domain.user.model.User

data class UserContext(
	var user: User = User(),
	val users: MutableList<User> = mutableListOf(),
) : BaseContext(command = UserCommand.NONE)

enum class UserCommand : IBaseCommand {
	NONE,
	CREATE_OWNER,
}
