package ru.md.msgal.db.folder.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "folder", schema = "gal", catalog = "medalist")
class FolderEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@Column(name = "parent_id")
	var parentId: Long? = null,

	var name: String? = null,

	var description: String? = null,

	@Column(name = "created_at")
	var createdAt: LocalDateTime? = null,

	@Column(name = "updated_at")
	var updatedAt: LocalDateTime? = null,

	) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val entity = other as FolderEntity
		return id == entity.id && parentId == entity.parentId && createdAt == entity.createdAt && name == entity.name
	}

	override fun hashCode(): Int {
		return Objects.hash(id, parentId, createdAt, name)
	}

	override fun toString(): String {
		return "Folder={id: $id, parentId: $parentId, name: $name}"
	}
}
