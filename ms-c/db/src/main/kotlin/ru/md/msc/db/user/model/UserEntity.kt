package ru.md.msc.db.user.model

import jakarta.persistence.*
import jakarta.persistence.CascadeType
import jakarta.persistence.OrderBy
import jakarta.persistence.Table
import org.hibernate.annotations.*
import ru.md.msc.db.dept.model.DeptEntity
import ru.md.msc.db.user.model.image.UserImageEntity
import ru.md.msc.db.user.model.role.RoleEntity
import ru.md.msc.domain.user.model.Gender
import java.io.Serializable
import java.util.*

@Entity
@Table(name = "user_data", schema = "users", catalog = "medalist")

@NamedEntityGraph(
	name = "withDept",
	attributeNodes = [NamedAttributeNode("dept")]
)

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

	@Column(name = "gender_code")
	var gender: Gender = Gender.UNDEF,

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "dept_id")
	var dept: DeptEntity? = null,

//	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = false)
//	val details: UserDetailsEntity? = null,

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
	@OrderBy("roleUser ASC")
	val roles: MutableList<RoleEntity> = mutableListOf(),

	@OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
	@JoinColumn(name = "user_id")
	@OrderBy("id DESC")
	val images: List<UserImageEntity> = emptyList(),
//	val images: MutableList<UserImageEntity> = mutableListOf(),

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
		return "User: {$id, $firstname, $authEmail}"
	}

}