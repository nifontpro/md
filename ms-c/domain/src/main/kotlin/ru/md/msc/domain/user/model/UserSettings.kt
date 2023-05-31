package ru.md.msc.domain.user.model

data class UserSettings(
	val userId: Long = 0,
	val showOnboarding: Boolean = false,
	val pageOnboarding: Int? = null,
	val notFound: Boolean = false
)