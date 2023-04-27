package ru.md.msc.domain.award.biz.proc

import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.domain.award.service.AwardService
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.IBaseCommand

class AwardContext : BaseContext() {
	var award: Award = Award()
	var modifyAward: Award = Award()
	var awardDetails: AwardDetails = AwardDetails()
	var awards: List<Award> = emptyList()

	lateinit var awardService: AwardService
}

enum class AwardCommand : IBaseCommand {
	CREATE,
	DELETE,
	UPDATE,
	GET_BY_ID_DETAILS,
	IMG_ADD,
	IMG_UPDATE,
	IMG_DELETE,
}
