package ru.md.msgal.db.item.model

import jakarta.persistence.*
import ru.md.base_domain.image.model.IBaseImage
import ru.md.base_domain.image.model.ImageType
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "item", schema = "gal", catalog = "medalist")
class ItemEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	override val id: Long? = null,

	@Column(name = "folder_id")
	var folderId: Long? = null,

	var name: String? = null,

	var description: String? = null,

	@Column(name = "created_at")
	override var createdAt: LocalDateTime? = null,

	@Column(name = "updated_at")
	var updatedAt: LocalDateTime? = null,

	@Column(name = "image_url")
	override var normalUrl: String? = null,

	@Column(name = "image_key")
	override var normalKey: String? = null,

	@Column(name = "mini_url")
	override val miniUrl: String? = null,

	@Column(name = "mini_key")
	override val miniKey: String? = null,

	@Column(name = "origin_key")
	override val originKey: String? = null,

	@Column(name = "origin_url")
	override val originUrl: String? = null,

	@Transient
	override val type: ImageType = ImageType.UNDEF,

	@Transient
	override var main: Boolean = false,

	) : IBaseImage {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val entity = other as ItemEntity
		return id == entity.id && normalKey == entity.normalKey
	}

	override fun hashCode(): Int {
		return Objects.hash(id, normalKey)
	}

	override fun toString(): String {
		return "Folder={id: $id, parentId: $folderId, name: $name, imageKey: $normalKey}\n"
	}
}
