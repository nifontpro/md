package ru.md.msc.domain.award.model

import ru.md.base_domain.dept.model.Dept
import ru.md.base_domain.user.model.User
import java.time.LocalDateTime

data class Award(
	val id: Long = 0,
	val name: String = "",
	val description: String? = null,
	val mainImg: String? = null,
	val normImg: String? = null,
	val type: AwardType = AwardType.UNDEF,
	val startDate: LocalDateTime = LocalDateTime.now(),
	val endDate: LocalDateTime = LocalDateTime.now(),
	val score: Int = 0,
	val state: AwardState = AwardState.ERROR,
	val dept: Dept = Dept(),
	val users: List<User> = emptyList(),
)