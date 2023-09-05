package ru.md.msc.domain.user.model

import ru.md.base_domain.image.model.BaseImage
import ru.md.msc.domain.award.model.Activity
import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.dept.model.Dept
import java.util.Collections.emptySet

data class User(
	val id: Long = 0,
	val dept: Dept? = null,
	val firstname: String = "",
	val patronymic: String? = null,
	val lastname: String? = null,
	val authEmail: String? = null,
	val gender: Gender = Gender.UNDEF,
	val post: String? = null,
	val roles: Set<RoleUser> = emptySet(),
	val images: List<BaseImage> = emptyList(),
	val mainImg: String? = null,
	val activities: List<Activity> = emptyList(),
	val awards: List<Award> = emptyList(),
	val awardCount: Long = 0,
	val scores: Long = 0,
)