package ru.md.msc.domain.award.service

import ru.md.msc.domain.award.model.AwardDetails

interface AwardService {
	fun create(awardDetails: AwardDetails): AwardDetails
}