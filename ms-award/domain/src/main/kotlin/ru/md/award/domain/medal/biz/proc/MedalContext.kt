package ru.md.award.domain.medal.biz.proc

import ru.md.award.domain.medal.model.Medal
import ru.md.award.domain.medal.model.MedalDetails
import ru.md.award.domain.medal.service.MedalImageService
import ru.md.award.domain.medal.service.MedalService
import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.base_domain.gallery.SmallItem

class MedalContext : BaseMedalsContext() {
	var medalId: Long = 0
	var medal: Medal = Medal()
	var medalDetails: MedalDetails = MedalDetails()
	var medals: List<Medal> = emptyList()

	var smallItem: SmallItem = SmallItem()

	lateinit var medalService: MedalService
	lateinit var medalImageService: MedalImageService
}

enum class MedalCommand : IBaseCommand {
	CREATE,
	DELETE,
	UPDATE,
	GET_BY_ID,
	IMG_ADD,
	IMG_DELETE,
	IMG_ADD_GALLERY
}
