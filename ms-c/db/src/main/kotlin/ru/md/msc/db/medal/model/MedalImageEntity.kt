package ru.md.msc.db.medal.model

import jakarta.persistence.*
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "medal_image", schema = "rew", catalog = "medalist")
class MedalImageEntity {
	@Basic
	@Column(name = "image_url")
	var imageUrl: String? = null

	@Basic
	@Column(name = "image_key")
	var imageKey: String? = null

	@Basic
	@Column(name = "type_code")
	var typeCode: String? = null

	@Basic
	@Column(name = "main")
	var isMain = false

	@Basic
	@Column(name = "created_at")
	var createdAt: Timestamp? = null

	@Basic
	@Column(name = "mini_url")
	var miniUrl: String? = null

	@Basic
	@Column(name = "mini_key")
	var miniKey: String? = null

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	var id: Long = 0

	@Basic
	@Column(name = "medal_id")
	var medalId: Long = 0
	override fun equals(o: Any?): Boolean {
		if (this === o) return true
		if (o == null || javaClass != o.javaClass) return false
		val that = o as MedalImageEntity
		return isMain == that.isMain && id == that.id && medalId == that.medalId && imageUrl == that.imageUrl && imageKey == that.imageKey && typeCode == that.typeCode && createdAt == that.createdAt && miniUrl == that.miniUrl && miniKey == that.miniKey
	}

	override fun hashCode(): Int {
		return Objects.hash(imageUrl, imageKey, typeCode, isMain, createdAt, miniUrl, miniKey, id, medalId)
	}
}
