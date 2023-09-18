package ru.md.msc.domain.medal.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.medal.model.Medal
import ru.md.msc.domain.medal.model.MedalDetails
import ru.md.msc.domain.medal.service.MedalService

class MedalContext : BaseClientContext() {
	var medal: Medal = Medal()
	var medalDetails: MedalDetails = MedalDetails()
	var medals: List<Medal> = emptyList()

	lateinit var medalService: MedalService
}

enum class MedalCommand : IBaseCommand {
	CREATE,
	DELETE,
	UPDATE,
	GET_BY_ID_DETAILS,
}
