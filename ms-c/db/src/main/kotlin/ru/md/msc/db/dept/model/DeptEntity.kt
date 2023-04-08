package ru.md.msc.db.dept.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import ru.md.msc.db.dept.model.image.DeptImageEntity
import java.util.*

@Entity
@Table(name = "dept", schema = "dep", catalog = "medalist")

@JsonIgnoreProperties
class DeptEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@Column(name = "parent_id")
	val parentId: Long? = null,

	val name: String? = null,

	val code: String? = null,

//	@OneToOne(mappedBy = "dept", fetch = FetchType.LAZY/*, optional = false*/)
//	val details: DeptDetailsEntity? = null,

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "dep_id")
	@Fetch(FetchMode.SUBSELECT)
	val images: List<DeptImageEntity> = emptyList()

//	@OneToMany(mappedBy = "dept", fetch = FetchType.LAZY)
//	@Fetch(FetchMode.SUBSELECT)
//	val users: List<UserEntity> = emptyList()

) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val deptEntity = other as DeptEntity
		return id == deptEntity.id && parentId == deptEntity.parentId && name == deptEntity.name && code == deptEntity.code
	}

	override fun hashCode(): Int {
		return Objects.hash(id, parentId, name, code)
	}

	override fun toString(): String {
		return "DeptEntity={id: $id, parentId: $parentId, name: $name}"
	}
}
