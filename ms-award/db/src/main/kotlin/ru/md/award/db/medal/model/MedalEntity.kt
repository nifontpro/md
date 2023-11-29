package ru.md.award.db.medal.model

import jakarta.persistence.*
import ru.md.base_db.dept.model.DeptEntity
import java.util.*

@NamedEntityGraph(
	name = "medalWithDept",
	attributeNodes = [NamedAttributeNode("deptEntity")]
)

//@NamedEntityGraph(
//	name = "medalWithDeptAndUser",
//	attributeNodes = [NamedAttributeNode("dept"), NamedAttributeNode("users")]
//)

@Entity
@Table(name = "medal", schema = "rew", catalog = "medalist")
class MedalEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	var name: String = "",

	@Column(name = "main_img")
	var mainImg: String? = null,

	@Column(name = "norm_img")
	var normImg: String? = null,

	var score: Int = 0,

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "dept_id")
	var deptEntity: DeptEntity? = null,

//	@Fetch(FetchMode.SUBSELECT)
//	@ManyToMany(fetch = FetchType.LAZY)
//	@JoinTable(
//		schema = "md", name = "activity",
//		joinColumns = [JoinColumn(name = "award_id")],
//		inverseJoinColumns = [JoinColumn(name = "user_id")]
//	)
//	@WhereJoinTable(clause = "is_activ=true and action_code='A'")
//	val users: List<UserEntity> = emptyList(),
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val medal = other as MedalEntity
		return id == medal.id && deptEntity?.id == medal.deptEntity?.id && score == medal.score
	}

	override fun hashCode(): Int {
		return Objects.hash(id, name, deptEntity?.id, score)
	}

	override fun toString(): String {
		return "\nMedal {id: $id, name: $name, score: $score}"
	}
}
