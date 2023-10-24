package ru.md.shop.db.pay.model

import jakarta.persistence.*
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "user_pay", schema = "shop", catalog = "medalist")
class UserPayEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	var id: Long = 0

	@Basic
	@Column(name = "user_id")
	var userId: Long = 0

	@Basic
	@Column(name = "balance")
	var balance = 0

	@Basic
	@Column(name = "created_at")
	var createdAt: Timestamp? = null
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as UserPayEntity
		return id == that.id && userId == that.userId && balance == that.balance && createdAt == that.createdAt
	}

	override fun hashCode(): Int {
		return Objects.hash(id, userId, balance, createdAt)
	}
}
