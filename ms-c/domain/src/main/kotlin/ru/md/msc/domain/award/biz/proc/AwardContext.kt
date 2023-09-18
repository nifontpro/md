package ru.md.msc.domain.award.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.msc.domain.award.model.*
import ru.md.msc.domain.award.service.AwardService
import ru.md.msc.domain.base.biz.BaseClientContext

class AwardContext : BaseClientContext() {
	var award: Award = Award()
	var awardDetails: AwardDetails = AwardDetails()
	var awards: List<Award> = emptyList()
	var activity: Activity = Activity()
	var activities: List<Activity> = emptyList()

	var userDeptId: Long = 0
	var actionType: ActionType = ActionType.UNDEF
	var awardState: AwardState? = null
	var awardType: AwardType? = null
	var withUsers: Boolean = false // Включать ли награжденных сотрудников при получении наград

	var awardsCount: List<AwardCount> = emptyList()
	var awardStateCount: AwardStateCount = AwardStateCount()
	var wwAwardCount: WWAwardCount = WWAwardCount()

	lateinit var awardService: AwardService
}

enum class AwardCommand : IBaseCommand {
	CREATE,
	DELETE,
	UPDATE,
	GET_BY_ID_DETAILS,
	GET_BY_DEPT,
	IMG_ADD,
	IMG_ADD_GALLERY,
	IMG_DELETE,
	ADD_ACTION,
	GET_ACTIVE_AWARD_BY_USER,
	GET_ACTIVE_AWARD_BY_DEPT,
	GET_USERS_BY_ACTIVE_AWARD,
	GET_ADMIN_AVAILABLE,
	GET_ADMIN_AVAILABLE_USER_EXCLUDE,
	GET_SIMPLE_AWARD_AVAILABLE_USER_EXCLUDE,
	COUNT_BY_DEPTS,
	COUNT_ACTIV,
	COUNT_ACTIV_ROOT,
	COUNT_USER_AWARD_WW,
	SET_MAIN_IMG
}
