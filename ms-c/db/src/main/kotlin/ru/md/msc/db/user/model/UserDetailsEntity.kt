package ru.md.msc.db.user.model

import jakarta.persistence.*
import ru.md.base_db.user.model.UserEntity
import ru.md.base_db.user.model.UserImageEntity
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "user_details", schema = "users", catalog = "medalist")

@NamedEntityGraph(
	name = "withUserDept",
	attributeNodes = [
		NamedAttributeNode(value = "user", subgraph = "userWithDept"),
	],
	subgraphs = [NamedSubgraph(name = "userWithDept", attributeNodes = [NamedAttributeNode("dept")])]
)

@NamedEntityGraph(
	name = "withUser",
	attributeNodes = [NamedAttributeNode(value = "user")]
)
class UserDetailsEntity(

	@Id
	@Column(name = "user_id")
	val userId: Long? = null,

	var phone: String? = null,
	var address: String? = null,
	var description: String? = null,

	@Column(name = "created_at")
	val createdAt: LocalDateTime? = null,

	@Column(name = "tab_id")
	var tabId: Long? = null, //  Табельный номер Сотрудника

	@OneToOne(fetch = FetchType.EAGER, optional = false, cascade = [CascadeType.PERSIST])
	@JoinColumn(name = "user_id")
	@MapsId
	var user: UserEntity,

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@OrderBy("id DESC")
	val images: List<UserImageEntity> = emptyList(),

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