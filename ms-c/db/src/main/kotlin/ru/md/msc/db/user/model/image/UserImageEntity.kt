package ru.md.msc.db.user.model.image

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
	override val imageUrl: String = "",

	@Column(name = "image_key")
	override val imageKey: String = "",

	@Column(name = "type_code")
	override val type: ImageType = ImageType.UNDEF,

	@Column(name = "main")
	override val main: Boolean = false,

	@Column(name = "created_at")
	override val createdAt: LocalDateTime? = null,

	@Column(name = "user_id")
	var userId: Long? = null,

	) : IBaseImage {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val userImage = other as UserImageEntity
		return id == userImage.id && imageUrl == userImage.imageUrl && imageKey == userImage.imageKey && type == userImage.type && main == userImage.main
	}

	override fun hashCode(): Int {
		return Objects.hash(imageUrl, imageKey, type, main, id)
	}

	override fun toString(): String {
		return "UserImage={id: $id, imageUrl: $imageUrl, type: $type}"
	}
}
