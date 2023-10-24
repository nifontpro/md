package ru.md.shop.db.pay.model

import jakarta.persistence.*
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "user_order", schema = "shop", catalog = "medalist")
class UserOrderEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	var id: Long = 0

	@Basic
	@Column(name = "user_id")
	var userId: Long = 0

	@Basic
	@Column(name = "status_code")
	var statusCode: String? = null

	@Basic
	@Column(name = "created_at")
	var createdAt: Timestamp? = null

	@Basic
	@Column(name = "cost")
	var cost = 0
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as UserOrderEntity
		return id == that.id && userId == that.userId && cost == that.cost && statusCode == that.statusCode && createdAt == that.createdAt
	}

	override fun hashCode(): Int {
		return Objects.hash(id, userId, statusCode, createdAt, cost)
	}
}
