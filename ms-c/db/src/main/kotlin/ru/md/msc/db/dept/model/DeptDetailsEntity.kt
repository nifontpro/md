package ru.md.msc.db.dept.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "dept_details", schema = "dep", catalog = "medalist")
class DeptDetailsEntity(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	var address: String? = null,
	var email: String? = null,
	var phone: String? = null,
	var description: String? = null,

	@Column(name = "created_at")
	val createdAt: LocalDateTime? = null,

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_id")
	@MapsId
	var dept: DeptEntity? = null,

	) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val details = other as DeptDetailsEntity
		return id == details.id && address == details.address && email == details.email && phone == details.phone && description == details.description && createdAt == details.createdAt
	}

	override fun hashCode(): Int {
		return Objects.hash(id, address, email, phone, description, createdAt)
	}

	override fun toString(): String {
		return "DeptDetails={id: $id, email:$email}"
	}
}
