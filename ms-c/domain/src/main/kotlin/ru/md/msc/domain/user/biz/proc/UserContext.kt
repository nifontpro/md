package ru.md.msc.domain.user.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.base_domain.user.model.RoleUser
import ru.md.base_domain.user.model.User
import ru.md.msc.domain.award.model.ActionType
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.event.service.EventService
import ru.md.msc.domain.user.model.GenderCount
import ru.md.msc.domain.user.model.UserAward
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.domain.user.model.UserSettings
import ru.md.msc.domain.user.model.excel.AddUserReport
import ru.md.msc.domain.user.model.excel.UpdateKey

class UserContext : BaseClientContext() {
	var userDetails: UserDetails = UserDetails()
	var users: List<User> = emptyList()
	var usersAwards: List<UserAward> = emptyList()
	var notValidRole: RoleUser? = null
	var deleteDeptId: Long? = null // При удалении собственного профиля владельца id его отдела

	var genderCount: GenderCount = GenderCount()
	var userSettings: UserSettings = UserSettings()

	// for exclude award:
	var actionType: ActionType? = null
	var hasOwnerRole: Boolean = false
	var fileUrl: String = ""
	val addReport: MutableList<AddUserReport> = mutableListOf()
	var updateKey: UpdateKey = UpdateKey.USER_TAB_NO

	lateinit var eventService: EventService
}

enum class UserCommand : IBaseCommand {
	CREATE,
	UPDATE,
	CREATE_OWNER,
	GET_PROFILES,
	GET_BY_SUB_DEPTS,
	GET_BY_SUB_DEPTS_EXCLUDE_AWARD,
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
	CHECK_HAS_OWNER_ROLE,
	ADD_FROM_EXCEL
}
