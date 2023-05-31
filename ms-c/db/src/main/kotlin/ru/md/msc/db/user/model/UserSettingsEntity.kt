package ru.md.msc.db.user.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serializable
import java.util.*

@Entity
@Table(name = "settings", schema = "users", catalog = "medalist")

class UserSettingsEntity(

	@Id
	@Column(name = "user_id")
	val userId: Long = 0,

	@Column(name = "show_onb")
	var showOnboarding: Boolean = false,

	@Column(name = "page_onb")
	var pageOnboarding: Int? = null,

	) : Serializable {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val settingsEntity = other as UserSettingsEntity
		return userId == settingsEntity.userId
	}

	override fun hashCode(): Int {
		return Objects.hash(userId, showOnboarding, pageOnboarding)
	}

	override fun toString(): String {
		return "UserDetails: {id: $userId,showOnboarding: $showOnboarding, pageOnboarding: $pageOnboarding}"
	}
}