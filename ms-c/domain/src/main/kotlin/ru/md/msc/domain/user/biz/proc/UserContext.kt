package ru.md.msc.domain.user.biz.proc

import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.IBaseCommand
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails

class UserContext : BaseContext(command = UserCommand.NONE) {
	var user: User = User()
	var userDetails: UserDetails = UserDetails()
	var users: List<User> = emptyList()
//	var usersDetails: List<UserDetails> = emptyList()
}

enum class UserCommand : IBaseCommand {
	NONE,
	CREATE_OWNER,
	GET_PROFILES,
	GET_BY_DEPT,
	GET_BY_ID_DETAILS,
	DELETE,
	IMG_ADD,
	IMG_DELETE,
}
