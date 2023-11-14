package ru.md.msc.db.dept.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "settings", schema = "dep", catalog = "medalist")
class DeptSettingsEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column(name = "dept_id")
	var deptId: Long = 0,

	@Column(name = "pay_name")
	var payName: String = ""

) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as DeptSettingsEntity
		return id == that.id && deptId == that.deptId && payName == that.payName
	}

	override fun hashCode(): Int {
		return Objects.hash(id, deptId, payName)
	}
}
