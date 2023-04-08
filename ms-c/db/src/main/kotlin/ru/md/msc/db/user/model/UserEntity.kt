package ru.md.msc.db.user.model

import jakarta.persistence.*
import jakarta.persistence.Table
import org.hibernate.annotations.*
import ru.md.msc.db.dept.model.DeptEntity
import ru.md.msc.domain.user.model.Gender
import ru.md.msc.db.user.model.image.UserImageEntity
import ru.md.msc.db.user.model.role.RoleEntity
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

	val email: String? = null,
	val firstname: String = "",
	val patronymic: String? = null,
	val lastname: String? = null,
	val post: String? = null,

	@Column(name = "gender_code")
	var gender: Gender = Gender.UNDEF,

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "dept_id")
	var dept: DeptEntity? = null,

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = false)
	val details: UserDetailsEntity? = null,

	@OneToMany(fetch = FetchType.LAZY/*, cascade = [CascadeType.ALL]*/)
	@JoinColumn(name = "user_id") // Если однонаправленная связь, без mappedBy
	@Fetch(FetchMode.SUBSELECT)
	val roles: List<RoleEntity> = emptyList(),

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@Fetch(FetchMode.SUBSELECT)
	val images: List<UserImageEntity> = emptyList()

) : Serializable {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val user = other as UserEntity
		return id == user.id
	}

	override fun hashCode(): Int {
		return Objects.hash(email + id)
	}

	override fun toString(): String {
		return "User: {$id, $firstname, $email}"
	}

}