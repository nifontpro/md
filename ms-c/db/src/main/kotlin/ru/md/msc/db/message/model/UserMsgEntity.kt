package ru.md.msc.db.message.model

import jakarta.persistence.*
import ru.md.msc.domain.message.model.MessageType
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "user_msg", schema = "msg", catalog = "medalist")
class UserMsgEntity (

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column(name = "from_id")
	var fromId: Long? = null,

	@Column(name = "to_id", nullable = false)
	var toId: Long = 0,

	@Column(name = "type", nullable = false)
	val type: MessageType = MessageType.NONE,

	var msg: String? = null,

	var read: Boolean = false,

	@Column(name = "send_date", nullable = false)
	var sendDate: LocalDateTime = LocalDateTime.now(),

	@Column(name = "image_url")
	var imageUrl: String? = null,
){

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val userMsg = other as UserMsgEntity
		return id == userMsg.id && fromId == userMsg.fromId && toId == userMsg.toId && type == userMsg.type && msg == userMsg.msg && sendDate == userMsg.sendDate
	}

	override fun hashCode(): Int {
		return Objects.hash(id, fromId, toId, type, msg, read, sendDate)
	}
}
