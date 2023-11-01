package ru.md.shop.db.product.model

import jakarta.persistence.*
import ru.md.base_domain.image.model.IBaseImage
import ru.md.base_domain.image.model.ImageType
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "product_image", schema = "shop", catalog = "medalist")
class ProductImageEntity(

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	override var id: Long? = null,

	@Column(name = "image_url")
	override var normalUrl: String? = null,

	@Column(name = "image_key")
	override var normalKey: String? = null,

	@Column(name = "mini_url")
	override var miniUrl: String? = null,

	@Column(name = "mini_key")
	override var miniKey: String? = null,

	@Column(name = "origin_key")
	override val originKey: String? = null,

	@Column(name = "origin_url")
	override val originUrl: String? = null,

	@Column(name = "type_code")
	override var type: ImageType = ImageType.UNDEF,

	@Column(name = "main")
	override var main: Boolean = false,

	@Column(name = "created_at")
	override var createdAt: LocalDateTime = LocalDateTime.now(),

	@Column(name = "product_id")
	var productId: Long = 0,

	) : IBaseImage {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as ProductImageEntity
		return main == that.main && id == that.id && productId == that.productId && normalUrl == that.normalUrl &&
				normalKey == that.normalKey && type == that.type && createdAt == that.createdAt &&
				miniUrl == that.miniUrl && miniKey == that.miniKey
	}

	override fun hashCode(): Int {
		return Objects.hash(normalUrl, normalKey, type, main, createdAt, miniUrl, miniKey, id, productId)
	}
}
