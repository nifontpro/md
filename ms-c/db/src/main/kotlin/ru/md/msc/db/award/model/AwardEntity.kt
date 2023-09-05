package ru.md.msc.db.award.model

import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Formula
import org.hibernate.annotations.WhereJoinTable
import ru.md.msc.db.award.model.image.AwardImageEntity
import ru.md.msc.db.dept.model.DeptEntity
import ru.md.msc.db.user.model.UserEntity
import ru.md.msc.domain.award.model.AwardState
import ru.md.msc.domain.award.model.AwardType
import java.time.LocalDateTime
import java.util.*

@NamedEntityGraph(
	name = "awardWithDept",
	attributeNodes = [NamedAttributeNode("dept")]
)

@NamedEntityGraph(
	name = "awardWithDeptAndUser",
	attributeNodes = [NamedAttributeNode("dept"), NamedAttributeNode("users")]
)

@Entity
@Table(name = "award", schema = "md", catalog = "medalist")
class AwardEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	var name: String = "",

	@Column(name = "main_img")
	var mainImg: String? = null,

	@Column(name = "type_code")
	var type: AwardType = AwardType.UNDEF,

	@Column(name = "start_date")
	var startDate: LocalDateTime = LocalDateTime.now(),

	@Column(name = "end_date")
	var endDate: LocalDateTime = LocalDateTime.now(),

	@Column
	var score: Int = 0,

	@Formula("award_state(start_date, end_date)")
	val state: AwardState = AwardState.ERROR,

//	@OneToOne(mappedBy = "award", fetch = FetchType.LAZY, optional = false)
//	val details: AwardDetailsEntity? = null,

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "dept_id")
	var dept: DeptEntity? = null,

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "award_id")
	@OrderBy("id DESC")
//	@Fetch(FetchMode.SUBSELECT)
	val images: List<AwardImageEntity> = emptyList(),

	@Fetch(FetchMode.SUBSELECT)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		schema = "md", name = "activity",
		joinColumns = [JoinColumn(name = "award_id")],
		inverseJoinColumns = [JoinColumn(name = "user_id")]
	)
	@WhereJoinTable(clause = "is_activ=true and action_code='A'")
	val users: List<UserEntity> = emptyList(),
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val award = other as AwardEntity
		return id == award.id && dept?.id == award.dept?.id && startDate == award.startDate &&
				endDate == award.endDate && name == award.name && type == award.type
	}

	override fun hashCode(): Int {
		return Objects.hash(id, startDate, endDate, name, type, dept?.id)
	}

	override fun toString(): String {
		return "Award={id: $id, name: $name}"
	}
}
