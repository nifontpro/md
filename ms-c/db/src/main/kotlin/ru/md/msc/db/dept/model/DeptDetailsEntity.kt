package ru.md.msc.db.dept.model

import jakarta.persistence.*
import ru.md.base_db.dept.model.DeptEntity
import ru.md.base_db.dept.model.DeptImageEntity
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "dept_details", schema = "dep", catalog = "medalist")

@NamedEntityGraph(
	name = "deptDetailsWithDept",
	attributeNodes = [NamedAttributeNode("dept")]
)

class DeptDetailsEntity(

	@Id
	@Column(name = "dept_id")
	val deptId: Long? = null,

	var address: String? = null,
	var email: String? = null,
	var phone: String? = null,
	var description: String? = null,

	@Column(name = "created_at")
	var createdAt: LocalDateTime? = null,

	@OneToOne(fetch = FetchType.EAGER, optional = false, cascade = [CascadeType.PERSIST])
	@JoinColumn(name = "dept_id")
	@MapsId
	var dept: DeptEntity,

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_id")
	@OrderBy("id DESC")
	val images: List<DeptImageEntity> = emptyList()

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
		return "DeptDetails={id: $deptId, email:$email, description: $description}"
	}
}
