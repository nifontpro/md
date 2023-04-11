package ru.md.msc.domain.user.biz.proc

import ru.md.base.dom.biz.BaseContext
import ru.md.base.dom.biz.IBaseCommand
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.domain.user.service.UserService

class UserContext : BaseContext(command = UserCommand.NONE) {
	var user: User = User()
	var userDetails: UserDetails = UserDetails()
	val users: MutableList<User> = mutableListOf()
	val usersDetails: MutableList<UserDetails> = mutableListOf()
	var emailVerified: Boolean = false

	lateinit var userService: UserService
}

enum class UserCommand : IBaseCommand {
	NONE,
	CREATE_OWNER,
}