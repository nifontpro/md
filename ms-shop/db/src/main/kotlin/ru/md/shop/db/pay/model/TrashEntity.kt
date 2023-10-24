package ru.md.shop.db.pay.model

import jakarta.persistence.*
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "trash", schema = "shop", catalog = "medalist")
class TrashEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	var id: Long = 0

	@Column(name = "user_id")
	var userId: Long = 0

	@Basic
	@Column(name = "product_id")
	var productId: Long = 0

	@Basic
	@Column(name = "add_at")
	var addAt: Timestamp? = null

	@Basic
	@Column(name = "quantity")
	var quantity = 0
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as TrashEntity
		return id == that.id && userId == that.userId && productId == that.productId && quantity == that.quantity && addAt == that.addAt
	}

	override fun hashCode(): Int {
		return Objects.hash(id, userId, productId, addAt, quantity)
	}
}
