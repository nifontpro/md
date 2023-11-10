package ru.md.base_db.dept.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Formula
import java.util.*

@Entity
@Table(name = "dept", schema = "dep", catalog = "medalist")
class CompanyEntity(
	@Id
	val id: Long? = null,

	@Formula(
		"""
		(select d.name from dep.dept d where d.id = (select * from dep.get_company_level_id(id)))
	"""
	)
	val name: String? = null,

	) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val deptEntity = other as CompanyEntity
		return id == deptEntity.id
	}

	override fun hashCode(): Int {
		return Objects.hash(id)
	}

	override fun toString(): String {
		return "Company={id: $id, company: $name}"
	}
}
