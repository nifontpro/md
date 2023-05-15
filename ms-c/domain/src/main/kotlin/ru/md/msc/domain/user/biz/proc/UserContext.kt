package ru.md.msc.domain.user.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.user.model.RoleUser
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails

class UserContext : BaseClientContext() {
	var user: User = User()
	var userDetails: UserDetails = UserDetails()
	var users: List<User> = emptyList()
	var notValidRole: RoleUser? = null

	var modifyUser: User = User()
	var isModifyUserHasAdminRole: Boolean = false

//	var usersDetails: List<UserDetails> = emptyList()
}

enum class UserCommand : IBaseCommand {
	NONE,
	CREATE,
	UPDATE,
	CREATE_OWNER,
	GET_PROFILES,
	GET_BY_DEPT,
	GET_BY_SUB_DEPTS,
	GET_BY_ID_DETAILS,
	DELETE,
	IMG_ADD,
	IMG_DELETE,
}
