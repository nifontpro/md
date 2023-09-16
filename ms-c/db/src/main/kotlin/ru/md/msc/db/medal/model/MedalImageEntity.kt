package ru.md.msc.db.medal.model

import jakarta.persistence.*
import ru.md.base_domain.image.model.ImageType
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "medal_image", schema = "rew", catalog = "medalist")
class MedalImageEntity(

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0,

	@Column(name = "image_url")
	var imageUrl: String = "",

	@Column(name = "image_key")
	var imageKey: String = "",

	@Column(name = "mini_url")
	var miniUrl: String = "",

	@Column(name = "mini_key")
	var miniKey: String = "",

	@Column(name = "type_code")
	var type: ImageType = ImageType.UNDEF,

	@Column(name = "main")
	var isMain: Boolean = false,

	@Column(name = "created_at")
	var createdAt: LocalDateTime? = null,

	@Column(name = "medal_id")
	var medalId: Long = 0,

	) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as MedalImageEntity
		return isMain == that.isMain && id == that.id && medalId == that.medalId && imageUrl == that.imageUrl &&
				imageKey == that.imageKey && type == that.type && createdAt == that.createdAt &&
				miniUrl == that.miniUrl && miniKey == that.miniKey
	}

	override fun hashCode(): Int {
		return Objects.hash(imageUrl, imageKey, type, isMain, createdAt, miniUrl, miniKey, id, medalId)
	}
}
