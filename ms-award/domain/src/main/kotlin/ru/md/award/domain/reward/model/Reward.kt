package ru.md.award.domain.reward.model

import ru.md.award.domain.medal.model.MedalObj
import ru.md.base_domain.user.model.User
import java.time.LocalDateTime

data class Reward(
	val id: Long = 0,
	val date: LocalDateTime = LocalDateTime.now(),
	val medalObj: MedalObj,
	val user: User,
)