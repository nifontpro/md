package ru.md.msc.rest.user.model.response

import ru.md.base_domain.image.model.BaseImage
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.user.model.Gender
import ru.md.msc.domain.user.model.RoleUser
import ru.md.msc.rest.award.model.response.ActivityResponse
import ru.md.msc.rest.award.model.response.AwardResponse
import java.util.*

data class UserResponse(
	val id: Long = 0,
	val dept: Dept? = null,
	val firstname: String = "",
	val patronymic: String? = null,
	val lastname: String? = null,
	val authEmail: String? = null,
	val gender: Gender = Gender.UNDEF,
	val post: String? = null,
	val awardCount: Long = 0,
	val mainImg: String? = null,
	val roles: Set<RoleUser> = Collections.emptySet(),
	val images: List<BaseImage> = emptyList(),
	val activities: List<ActivityResponse> = emptyList(),
	val awards: List<AwardResponse> = emptyList(),
)
