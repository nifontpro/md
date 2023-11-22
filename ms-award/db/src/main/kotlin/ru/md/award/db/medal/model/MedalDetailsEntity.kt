package ru.md.award.db.medal.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "medal_details", schema = "rew", catalog = "medalist")
class MedalDetailsEntity(

	@Id
	@Column(name = "medal_id")
	var medalId: Long = 0,

	var description: String? = null,

	@Column(name = "created_at")
	var createdAt: LocalDateTime = LocalDateTime.MIN,

	@MapsId
	@JoinColumn(name = "medal_id")
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	var medalEntity: MedalEntity,

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "medal_id")
	@OrderBy("id DESC")
//	@Fetch(FetchMode.SUBSELECT)
	val images: List<MedalImageEntity> = emptyList(),

	) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as MedalDetailsEntity
		return medalId == that.medalId && description == that.description && createdAt == that.createdAt
	}

	override fun hashCode(): Int {
		return Objects.hash(medalId, description, createdAt)
	}
}
