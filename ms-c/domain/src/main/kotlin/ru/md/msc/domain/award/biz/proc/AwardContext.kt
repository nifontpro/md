package ru.md.msc.domain.award.biz.proc

import ru.md.msc.domain.award.model.ActionType
import ru.md.msc.domain.award.model.Activity
import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.domain.award.service.AwardService
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.IBaseCommand

class AwardContext : BaseContext() {
	var award: Award = Award()
	var awardDetails: AwardDetails = AwardDetails()
	var awards: List<Award> = emptyList()
	var activity: Activity = Activity()
	var activities: List<Activity> = emptyList()

	var awardId: Long = 0
	var userDeptId: Long = 0
	var actionType: ActionType = ActionType.UNDEF

	lateinit var awardService: AwardService
}

enum class AwardCommand : IBaseCommand {
	CREATE,
	DELETE,
	UPDATE,
	GET_BY_ID_DETAILS,
	GET_BY_DEPT,
	IMG_ADD,
	IMG_DELETE,
	ADD_ACTION,
	GET_ACTIVE_AWARD_BY_USER,
	GET_ACTIVE_AWARD_BY_DEPT,
	GET_USERS_BY_ACTIVE_AWARD,
	GET_ADMIN_AVAILABLE,
}
