package ru.md.award.domain.medal.service

import ru.md.award.domain.medal.model.Medal
import ru.md.award.domain.medal.model.MedalDetails

interface MedalService {
	fun create(medalDetails: MedalDetails): MedalDetails
	fun update(medalDetails: MedalDetails): MedalDetails
	fun findMedalById(medalId: Long): Medal
	fun findMedalDetailsById(medalId: Long): MedalDetails
	fun deleteById(medalId: Long)
	fun findDeptIdByMedalId(medalId: Long): Long
}