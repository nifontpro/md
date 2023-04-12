package ru.md.msc.domain.user.biz.proc

import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.IBaseCommand
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails

class UserContext : BaseContext(command = UserCommand.NONE) {
	var user: User = User()
	var userDetails: UserDetails = UserDetails()
	val users: MutableList<User> = mutableListOf()
	val usersDetails: MutableList<UserDetails> = mutableListOf()
}

enum class UserCommand : IBaseCommand {
	NONE,
	CREATE_OWNER,
}
