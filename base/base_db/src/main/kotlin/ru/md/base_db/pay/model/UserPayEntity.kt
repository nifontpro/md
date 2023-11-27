package ru.md.base_db.pay.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "user_pay", schema = "shop", catalog = "medalist")
class UserPayEntity(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column(name = "user_id")
	var userId: Long = 0,

	var balance: Int = 0,

	@Column(name = "created_at")
	var createdAt: LocalDateTime = LocalDateTime.now(),

	) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as UserPayEntity
		return id == that.id && userId == that.userId && balance == that.balance && createdAt == that.createdAt
	}

	override fun hashCode(): Int {
		return Objects.hash(id, userId, balance, createdAt)
	}

	override fun toString(): String {
		return "UserPay {id: $id, userId: $userId, balance: $balance}"
	}
}
