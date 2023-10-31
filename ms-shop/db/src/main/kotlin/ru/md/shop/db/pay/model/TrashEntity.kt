package ru.md.shop.db.pay.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "trash", schema = "shop", catalog = "medalist")
class TrashEntity(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0,

	@Column(name = "user_id")
	var userId: Long = 0,

	@Column(name = "product_id")
	var productId: Long = 0,

	@Column(name = "add_at")
	var addAt: LocalDateTime = LocalDateTime.now(),

	var quantity: Int = 0
) {

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
