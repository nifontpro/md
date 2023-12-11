package ru.md.award.db.activity.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "act_user", schema = "rew", catalog = "medalist")
class ActUserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null

	@Column(name = "act_id")
	var actId: Long = 0

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
