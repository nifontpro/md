package ru.md.msc.db.award.model

import jakarta.persistence.*
import ru.md.msc.db.award.model.image.AwardImageEntity
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "award_details", schema = "md", catalog = "medalist")
class AwardDetailsEntity(

	@Id
	@Column(name = "award_id")
	var id: Long? = null,

	var criteria: String? = null,

	@Column(name = "created_at")
	val createdAt: LocalDateTime? = null,

	@OneToOne(fetch = FetchType.LAZY, optional = false, cascade = [CascadeType.PERSIST])
	@JoinColumn(name = "award_id")
	@MapsId
	val award: AwardEntity,

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "award_id")
	@OrderBy("id DESC")
//	@Fetch(FetchMode.SUBSELECT)
	val images: List<AwardImageEntity> = emptyList(),

	) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as AwardDetailsEntity
		return id == that.id  && criteria == that.criteria && createdAt == that.createdAt
	}

	override fun hashCode(): Int {
		return Objects.hash(id, criteria, createdAt)
	}
}
