package ru.md.msc.db.medal.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "act_user", schema = "rew", catalog = "medalist")
class ActUserEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	var id: Long = 0

	@Basic
	@Column(name = "act_id")
	var actId: Long = 0

	@Basic
	@Column(name = "user_id")
	var userId: Long = 0
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as ActUserEntity
		return id == that.id && actId == that.actId && userId == that.userId
	}

	override fun hashCode(): Int {
		return Objects.hash(id, actId, userId)
	}
}
