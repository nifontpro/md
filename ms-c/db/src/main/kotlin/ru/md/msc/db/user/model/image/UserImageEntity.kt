package ru.md.msc.db.user.model.image

import jakarta.persistence.*
import ru.md.msc.domain.base.model.BaseImage
import ru.md.msc.domain.base.model.ImageType
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "user_image", schema = "users", catalog = "medalist")
class UserImageEntity(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@Column(name = "image_url")
	override val imageUrl: String? = null,

	@Column(name = "image_key")
	override val imageKey: String? = null,

	@Column(name = "type_code")
	override val type: ImageType = ImageType.UNDEF,

	@Column(name = "main")
	override val main: Boolean? = null,

	@Column(name = "created_at")
	override val createdAt: Timestamp? = null,

//	@ManyToOne(optional = false, fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_id")
//	val user: UserEntity? = null,

	@Column(name = "user_id")
	val userId: Long? = null,

	) : BaseImage {

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
		return "UserImage={id: $id, userId: $userId, imageUrl: $imageUrl, type: $type}"
	}
}
