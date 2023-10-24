package ru.md.shop.db.pay.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "order_data", schema = "shop", catalog = "medalist")
class OrderDataEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	var id: Long = 0

	@Basic
	@Column(name = "oder_id")
	var oderId: Long = 0

	@Basic
	@Column(name = "product_id")
	var productId: Long = 0

	@Basic
	@Column(name = "price_at_purchase")
	var priceAtPurchase = 0

	@Basic
	@Column(name = "quantity")
	var quantity = 0
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as OrderDataEntity
		return id == that.id && oderId == that.oderId && productId == that.productId && priceAtPurchase == that.priceAtPurchase && quantity == that.quantity
	}

	override fun hashCode(): Int {
		return Objects.hash(id, oderId, productId, priceAtPurchase, quantity)
	}
}
