package ru.md.msc.domain.user.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.user.model.*

class UserContext : BaseClientContext() {
	var user: User = User()
	var userDetails: UserDetails = UserDetails()
	var users: List<User> = emptyList()
	var notValidRole: RoleUser? = null

	var genderCount: GenderCount = GenderCount()
	var userSettings: UserSettings = UserSettings()

//	var usersDetails: List<UserDetails> = emptyList()
}

enum class UserCommand : IBaseCommand {
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
	GENDER_COUNT_BY_DEPTS,
	GET_WITH_ACTIVITY,
	GET_WITH_AWARDS,
	GET_WITH_AWARD_COUNT,
	SET_MAIN_IMG,
	SAVE_SETTINGS,
	GET_SETTINGS,
	GET_AUTH_DEPT_ID
}
