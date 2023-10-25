package ru.md.base_db.user.model

import jakarta.persistence.*
import ru.md.base_domain.image.model.IBaseImage
import ru.md.base_domain.image.model.ImageType
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "user_image", schema = "users", catalog = "medalist")
class UserImageEntity(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	override val id: Long? = null,

	@Column(name = "image_url")
	override val normalUrl: String? = null,

	@Column(name = "image_key")
	override val normalKey: String? = null,

	@Column(name = "mini_url")
	override val miniUrl: String? = null,

	@Column(name = "mini_key")
	override val miniKey: String? = null,

	@Column(name = "origin_key")
	override val originKey: String? = null,

	@Column(name = "origin_url")
	override val originUrl: String? = null,

	@Column(name = "type_code")
	override val type: ImageType = ImageType.UNDEF,

	@Column(name = "main")
	override var main: Boolean = false,

	@Column(name = "created_at")
	override val createdAt: LocalDateTime = LocalDateTime.now(),

	@Column(name = "user_id")
	var userId: Long? = null,

	) : IBaseImage {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val userImage = other as UserImageEntity
		return id == userImage.id && normalUrl == userImage.normalUrl && normalKey == userImage.normalKey && type == userImage.type && main == userImage.main
	}

	override fun hashCode(): Int {
		return Objects.hash(normalUrl, normalKey, type, main, id)
	}

	override fun toString(): String {
		return "UserImage={id: $id, imageUrl: $normalUrl, type: $type}"
	}
}
