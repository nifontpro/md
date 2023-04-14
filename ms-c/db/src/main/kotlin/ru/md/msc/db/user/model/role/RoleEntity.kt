package ru.md.msc.db.user.model.role

import jakarta.persistence.*
import ru.md.msc.db.user.model.UserEntity
import ru.md.msc.domain.user.model.RoleUser
import java.util.*

@Entity
@Table(name = "user_roles", schema = "users", catalog = "medalist")
//@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
class RoleEntity(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@Column(name = "role_code")
	val roleUser: RoleUser,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	var user: UserEntity? = null,

//	) : Comparable<RolesEntity> {
	)  {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val role = other as RoleEntity
		return id == role.id && roleUser == role.roleUser
	}

	override fun hashCode(): Int {
		return Objects.hash(roleUser, id)
	}

	override fun toString(): String {
		return "RoleEntity: {$id, $roleUser}"
	}

//	override fun compareTo(other: RolesEntity): Int {
//		return roleEnum.compareTo(other.roleEnum)
//	}
}