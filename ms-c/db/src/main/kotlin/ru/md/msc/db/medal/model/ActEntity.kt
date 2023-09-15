package ru.md.msc.db.medal.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "act", schema = "rew", catalog = "medalist")
class ActEntity(
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column(name = "name")
	var name: String = "",

	@Column(name = "class_code")
	var classCode: String? = null,

	var description: String? = null,

	@Column(name = "start_date")
	var startDate: LocalDateTime? = null,

	@Column(name = "result_date")
	var resultDate: LocalDateTime? = null,

	@Column(name = "end_date")
	var endDate: LocalDateTime? = null,

	) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val actEntity = other as ActEntity
		return id == actEntity.id && name == actEntity.name && classCode == actEntity.classCode && description == actEntity.description && startDate == actEntity.startDate && resultDate == actEntity.resultDate && endDate == actEntity.endDate
	}

	override fun hashCode(): Int {
		return Objects.hash(id, name, classCode, description, startDate, resultDate, endDate)
	}
}
