package ru.md.shop.db.product.model

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "product_details", schema = "shop", catalog = "medalist")
class ProductDetailsEntity(

	@Id
	@Column(name = "product_id")
	val productId: Long = 0,

	@Column(name = "description")
	var description: String? = null,

	@Column(name = "site_url")
	var siteUrl: String? = null,

	@Column(name = "created_at")
	var createdAt: LocalDateTime = LocalDateTime.now(),

	@OneToOne(fetch = FetchType.EAGER, optional = false, cascade = [CascadeType.PERSIST])
	@JoinColumn(name = "product_id")
	@MapsId
	var productEntity: ProductEntity,

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@OrderBy("id DESC")
	val images: List<ProductImageEntity> = emptyList(),

	): Serializable {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as ProductDetailsEntity
		return productId == that.productId && description == that.description && createdAt == that.createdAt
	}

	override fun hashCode(): Int {
		return Objects.hash(productId, description, createdAt)
	}

	override fun toString(): String {
		return "ProductDetails: {id: $productId, createdAt: $createdAt}"
	}
}