package ru.md.msc.db.event.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "dept_event", schema = "env", catalog = "medalist")
class DeptEventEntity(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column(name = "event_date")
	var eventDate: LocalDateTime = LocalDateTime.now(),

	@Column(name = "dept_id")
	var deptId: Long = 0,

	@Column(name = "name")
	var eventName: String = "",
) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val deptEvent = other as DeptEventEntity
		return id == deptEvent.id && eventDate == deptEvent.eventDate && deptId == deptEvent.deptId && eventName == deptEvent.eventName
	}

	override fun hashCode(): Int {
		return Objects.hash(id, eventDate, deptId, eventName)
	}
}
