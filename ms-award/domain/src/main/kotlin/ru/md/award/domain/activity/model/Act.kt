package ru.md.award.domain.activity.model

import ru.md.award.domain.medal.model.MedalObj
import ru.md.base_domain.user.model.User
import java.time.LocalDateTime

data class Act(
	val id: Long = 0,
	val name: String = "",
	val actClass: ActClass = ActClass.UNDEF,
	val description: String? = null,
	val startDate: LocalDateTime? = null,
	val resultDate: LocalDateTime? = null,
	val endDate: LocalDateTime? = null,
	val medals: List<MedalObj> = emptyList(),
	val users: List<User> = emptyList(),
)