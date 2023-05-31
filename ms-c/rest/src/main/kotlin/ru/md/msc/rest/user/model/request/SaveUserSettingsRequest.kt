package ru.md.msc.rest.user.model.request

data class SaveUserSettingsRequest(
	val userId: Long = 0,
	val showOnboarding: Boolean = false,
	val pageOnboarding: Int? = null
)