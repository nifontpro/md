package ru.md.msc.db.award.model

import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import ru.md.msc.db.award.model.image.AwardImageEntity
import ru.md.msc.db.dept.model.DeptEntity
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "award", schema = "md", catalog = "medalist")
class AwardEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "dept_id")
	var dept: DeptEntity? = null,

	@Column(name = "start_date")
	var startDate: Timestamp? = null,

	@Column(name = "end_date")
	var endDate: Timestamp? = null,

	var name: String? = null,

	@Column(name = "type_code")
	var typeCode: String? = null,

	@OneToOne(mappedBy = "award", fetch = FetchType.LAZY, optional = false)
	val details: AwardDetailsEntity? = null,

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "award_id")
	@Fetch(FetchMode.SUBSELECT)
	val images: List<AwardImageEntity> = emptyList()

) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val award = other as AwardEntity
		return id == award.id && dept?.id == award.dept?.id && startDate == award.startDate && endDate == award.endDate && name == award.name && typeCode == award.typeCode
	}

	override fun hashCode(): Int {
		return Objects.hash(id, startDate, endDate, name, typeCode, dept?.id)
	}

	override fun toString(): String {
		return "Award={id: $id, name: $name}"
	}
}
