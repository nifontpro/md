package ru.md.msc.db.medal.model

import jakarta.persistence.*
import ru.md.base_domain.image.model.IBaseImage
import ru.md.base_domain.image.model.ImageType
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "medal_image", schema = "rew", catalog = "medalist")
class MedalImageEntity(

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	override var id: Long = 0,

	@Column(name = "image_url")
	override var imageUrl: String = "",

	@Column(name = "image_key")
	override var imageKey: String = "",

	@Column(name = "mini_url")
	override var miniUrl: String = "",

	@Column(name = "mini_key")
	override var miniKey: String = "",

	@Column(name = "type_code")
	override var type: ImageType = ImageType.UNDEF,

	@Column(name = "main")
	override var main: Boolean = false,

	@Column(name = "created_at")
	override var createdAt: LocalDateTime = LocalDateTime.now(),

	@Column(name = "medal_id")
	var medalId: Long = 0,

	) : IBaseImage {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as MedalImageEntity
		return main == that.main && id == that.id && medalId == that.medalId && imageUrl == that.imageUrl &&
				imageKey == that.imageKey && type == that.type && createdAt == that.createdAt &&
				miniUrl == that.miniUrl && miniKey == that.miniKey
	}

	override fun hashCode(): Int {
		return Objects.hash(imageUrl, imageKey, type, main, createdAt, miniUrl, miniKey, id, medalId)
	}
}
