package ru.md.shop.db.product.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "product", schema = "shop", catalog = "medalist")
class ProductEntity(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column(name = "dept_id")
	var deptId: Long = 0,

	@Column(name = "name")
	var name: String = "",

	@Column(name = "price")
	var price: Int = 0,

	@Column(name = "qnt")
	var count: Int = 0,

	@Column(name = "main_img")
	var mainImg: String? = null,

	@Column(name = "norm_img")
	var normImg: String? = null,

	) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as ProductEntity
		return id == that.id && deptId == that.deptId && price == that.price && name == that.name && mainImg == that.mainImg && normImg == that.normImg
	}

	override fun hashCode(): Int {
		return Objects.hash(id, deptId, name, price, mainImg, normImg)
	}

	override fun toString(): String {
		return "Product: {id: $id, deptId: $deptId, name: $name, price: $price}"
	}
}
