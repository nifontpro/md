package ru.md.msc.db.dept.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import ru.md.msc.db.dept.model.image.DeptImageEntity
import ru.md.msc.domain.dept.model.DeptType
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
	var name: String = "",
	var classname: String? = null,

	@Column(name = "top_level", nullable = false)
	var topLevel: Boolean = false,

	@Column(name = "main_img")
	var mainImg: String? = null,

	@Column(name = "code")
	val type: DeptType = DeptType.UNDEF,

//	@OneToOne(mappedBy = "dept", fetch = FetchType.LAZY, optional = false)
//	val details: DeptDetailsEntity? = null,

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_id")
	@OrderBy("id DESC")
	val images: MutableList<DeptImageEntity> = mutableListOf(),

//	@OneToMany(mappedBy = "dept", fetch = FetchType.LAZY)
//	@Fetch(FetchMode.SUBSELECT)
//	val users: List<UserEntity> = emptyList()

) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val deptEntity = other as DeptEntity
		return id == deptEntity.id && parentId == deptEntity.parentId && name == deptEntity.name && type == deptEntity.type
	}

	override fun hashCode(): Int {
		return Objects.hash(id, parentId, name, type)
	}

	override fun toString(): String {
		return "DeptEntity={id: $id, parentId: $parentId, name: $name}"
	}
}
