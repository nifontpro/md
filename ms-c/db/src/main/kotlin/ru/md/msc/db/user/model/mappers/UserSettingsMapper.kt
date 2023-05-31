package ru.md.msc.db.user.model.mappers

import ru.md.msc.db.user.model.UserSettingsEntity
import ru.md.msc.domain.user.model.UserSettings

fun UserSettingsEntity.toUserSettings() = UserSettings(
	userId = userId,
	showOnboarding = showOnboarding,
	pageOnboarding = pageOnboarding
)

fun UserSettings.toUserSettingsEntity() = UserSettingsEntity(
	userId = userId,
	showOnboarding = showOnboarding,
	pageOnboarding = pageOnboarding
)