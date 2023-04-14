package ru.md.msc.rest.user.model.request

import com.fasterxml.jackson.annotation.JsonIgnore

data class GetProfilesRequest(
	@JsonIgnore
	val emptyField: String? = null
)
