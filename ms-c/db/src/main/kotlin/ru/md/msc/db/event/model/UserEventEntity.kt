package ru.md.msc.db.event.model

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "user_event", schema = "env", catalog = "medalist")
class UserEventEntity(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column(name = "event_date")
	var eventDate: LocalDateTime = LocalDateTime.now(),

	@Column(name = "name")
	var eventName: String = "",

	@Column(name = "user_id")
	var userId: Long = 0,

) : Serializable {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val userEvent = other as UserEventEntity
		return id == userEvent.id && eventDate == userEvent.eventDate && userId == userEvent.userId && eventName == userEvent.eventName
	}

	override fun hashCode(): Int {
		return Objects.hash(id, eventDate, userId, eventName)
	}

	override fun toString(): String {
		return "UserEventEntity {id: $id, eventName: $eventName, date: $eventDate, userId: $userId}"
	}
}
