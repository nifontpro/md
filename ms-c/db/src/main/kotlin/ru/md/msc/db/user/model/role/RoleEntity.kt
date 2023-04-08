package ru.md.msc.db.user.model.role

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user_role", schema = "users", catalog = "medalist")
//@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@IdClass(RoleId::class)
class RoleEntity(

	@Id
//	@Column(name = "user_id")
	val userId: Long = 0,

	@Id
	val code: String = "",
//	val roleEnum: RoleEnum,

	/*	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	//	@MapsId
	//	@JoinColumn(name = "user_id")
		var user: UserEntity,*/

) : Comparable<RoleEntity> {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val role = other as RoleEntity
		return userId == role.userId && code == role.code
	}

	override fun hashCode(): Int {
		return Objects.hash(code + userId)
	}

	override fun toString(): String {
		return "RoleEntity: {$userId, $code}"
	}

	override fun compareTo(other: RoleEntity): Int {
		return code.compareTo(other.code)
	}
}