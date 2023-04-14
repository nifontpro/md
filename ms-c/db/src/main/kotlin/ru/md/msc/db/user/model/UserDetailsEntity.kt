package ru.md.msc.db.user.model

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "user_details", schema = "users", catalog = "medalist")
/*@NamedEntityGraph(
	name = "withUserRoles",
	attributeNodes = [
		NamedAttributeNode(value = "user", subgraph = "userWithRoles"),
	],
	subgraphs = [NamedSubgraph(name = "userWithRoles", attributeNodes = [NamedAttributeNode("roles")])]
)*/

@NamedEntityGraph(
	name = "withUserRoles",
	attributeNodes = [NamedAttributeNode(value = "user")]
)
class UserDetailsEntity(

	@Id
	@Column(name = "user_id")
	val userId: Long? = null,

	val phone: String? = null,
	val address: String? = null,
	val description: String? = null,

	@Column(name = "created_at")
	val createdAt: LocalDateTime? = null,

	@OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
	@JoinColumn(name = "user_id")
	@MapsId
	val user: UserEntity? = null

) : Serializable {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val details = other as UserDetailsEntity
		return userId == details.userId && phone == details.phone && address == details.address && description == details.description && createdAt == details.createdAt
	}

	override fun hashCode(): Int {
		return Objects.hash(userId, phone, address, description, createdAt)
	}

	override fun toString(): String {
		return "UserDetails: {id: $userId, phone: $phone, address: $address}"
	}
}