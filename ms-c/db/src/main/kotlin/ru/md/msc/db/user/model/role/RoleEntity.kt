package ru.md.msc.db.user.model.role

import jakarta.persistence.*
import ru.md.msc.db.user.model.UserEntity
import ru.md.msc.domain.user.model.RoleEnum
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
	val roleEnum: RoleEnum,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	var user: UserEntity? = null,

//	) : Comparable<RolesEntity> {
	)  {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val role = other as RoleEntity
		return id == role.id && roleEnum == role.roleEnum
	}

	override fun hashCode(): Int {
		return Objects.hash(roleEnum, id)
	}

	override fun toString(): String {
		return "RoleEntity: {$id, $roleEnum}"
	}

//	override fun compareTo(other: RolesEntity): Int {
//		return roleEnum.compareTo(other.roleEnum)
//	}
}