package ru.md.msc.db.dept.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "dept_details", schema = "dep", catalog = "medalist")
class DeptDetailsEntity(

	@Id
	@Column(name = "dept_id")
	val deptId: Long? = null,

	val address: String? = null,
	val email: String? = null,
	val phone: String? = null,
	val description: String? = null,

	@Column(name = "created_at")
	val createdAt: LocalDateTime? = null,

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_id")
	@MapsId
	val dept: DeptEntity? = null,

	) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val details = other as DeptDetailsEntity
		return deptId == details.deptId && address == details.address && email == details.email && phone == details.phone && description == details.description && createdAt == details.createdAt
	}

	override fun hashCode(): Int {
		return Objects.hash(deptId, address, email, phone, description, createdAt)
	}

	override fun toString(): String {
		return "DeptDetails={id: $deptId, email:$email}"
	}
}
