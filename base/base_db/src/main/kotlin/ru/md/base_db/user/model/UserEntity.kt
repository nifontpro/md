package ru.md.base_db.user.model

import jakarta.persistence.*
import ru.md.base_db.dept.model.DeptEntity
import ru.md.base_domain.user.model.Gender
import java.io.Serializable
import java.util.*

@NamedEntityGraph(
	name = "withDept",
	attributeNodes = [NamedAttributeNode("dept")]
)

@NamedEntityGraph(
	name = "userWithDeptAndCompany",
	attributeNodes = [
		NamedAttributeNode("dept", subgraph = "deptWithCompany"),
	],
	subgraphs = [NamedSubgraph(name = "deptWithCompany", attributeNodes = [NamedAttributeNode("company")])]
)

@Entity
@Table(name = "user_data", schema = "users", catalog = "medalist")
class UserEntity(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@Column(name = "auth_email")
	var authEmail: String? = null,
	var firstname: String = "",
	var patronymic: String? = null,
	var lastname: String? = null,
	var post: String? = null,

	@Column(name = "main_img")
	var mainImg: String? = null,

	@Column(name = "norm_img")
	var normImg: String? = null,

	@Column(name = "gender_code")
	var gender: Gender = Gender.UNDEF,

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "dept_id")
	var dept: DeptEntity? = null,

//	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = false)
//	val details: UserDetailsEntity? = null,

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
	@OrderBy("roleUser ASC")
//	@Fetch(FetchMode.SUBSELECT)
	var roles: MutableList<RoleEntity> = mutableListOf(),

//	@Fetch(FetchMode.SUBSELECT)
//	@ManyToMany(fetch = FetchType.LAZY)
//	@JoinTable(
//		schema = "md", name = "activity",
//		joinColumns = [JoinColumn(name = "user_id")],
//		inverseJoinColumns = [JoinColumn(name = "award_id")]
//	)
//	@WhereJoinTable(clause = "is_activ=true and action_code='A'")
//	val awards: List<AwardEntity> = emptyList(),
//
//	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//	@Fetch(FetchMode.SUBSELECT)
//	@Where(clause = "is_activ=true and action_code='A'")
//	val activities: List<ActivityEntity> = emptyList(),

	@Column(name = "archive")
	val archive: Boolean = false

) : Serializable {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val user = other as UserEntity
		return id == user.id
	}

	override fun hashCode(): Int {
		return Objects.hash(authEmail + id)
	}

	override fun toString(): String {
		return "\nUser: {$id: $firstname, email: $authEmail, deptId: ${dept?.id}}"
	}

}