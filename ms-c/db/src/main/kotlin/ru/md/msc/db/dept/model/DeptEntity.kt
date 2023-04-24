package ru.md.msc.db.dept.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
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
	val parentId: Long = 0, // Если будет получение ROOT - исправить на nullable
	var name: String = "",
	var classname: String? = null,

	@Column(name = "code")
	val type: DeptType = DeptType.UNDEF,

	@OneToOne(mappedBy = "dept", fetch = FetchType.LAZY, optional = false)
	val details: DeptDetailsEntity? = null,

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "dept_id")
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
		return id == deptEntity.id && parentId == deptEntity.parentId && name == deptEntity.name && type == deptEntity.type
	}

	override fun hashCode(): Int {
		return Objects.hash(id, parentId, name, type)
	}

	override fun toString(): String {
		return "DeptEntity={id: $id, parentId: $parentId, name: $name}"
	}
}
