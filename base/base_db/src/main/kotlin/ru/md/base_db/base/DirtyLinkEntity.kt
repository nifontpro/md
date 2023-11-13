package ru.md.base_db.base

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "dirty_link", schema = "srv", catalog = "medalist")
class DirtyLinkEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	var key: String = "",

	var repo: String = "",

	@Column(name = "save_as")
	var saveAs: LocalDateTime = LocalDateTime.now()

) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as DirtyLinkEntity
		return id == that.id && key == that.key && repo == that.repo && saveAs == that.saveAs
	}

	override fun hashCode(): Int {
		return Objects.hash(id, key, repo, saveAs)
	}
}
