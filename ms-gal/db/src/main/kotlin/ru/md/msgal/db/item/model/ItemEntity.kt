package ru.md.msgal.db.item.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "item", schema = "gal", catalog = "medalist")
class ItemEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@Column(name = "folder_id")
	var folderId: Long? = null,

	var name: String? = null,

	var description: String? = null,

	@Column(name = "created_at")
	var createdAt: LocalDateTime? = null,

	@Column(name = "updated_at")
	var updatedAt: LocalDateTime? = null,

	@Column(name = "image_url")
	var imageUrl: String? = null,

	@Column(name = "image_key")
	var imageKey: String? = null,

	@Column(name = "mini_url")
	val miniUrl: String? = null,

	@Column(name = "mini_key")
	val miniKey: String? = null,

	) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val entity = other as ItemEntity
		return id == entity.id && imageKey == entity.imageKey
	}

	override fun hashCode(): Int {
		return Objects.hash(id, imageKey)
	}

	override fun toString(): String {
		return "Folder={id: $id, parentId: $folderId, name: $name, imageKey: $imageKey}\n"
	}
}
