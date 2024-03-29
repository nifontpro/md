package ru.md.msc.db.msg.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.msc.db.msg.model.mappers.toUserMsg
import ru.md.msc.db.msg.model.mappers.toUserMsgEntity
import ru.md.msc.db.msg.repo.MessageRepository
import ru.md.msc.domain.msg.biz.proc.MessageNotFoundException
import ru.md.msc.domain.msg.model.UserMsg
import ru.md.msc.domain.msg.service.MessageService

@Service
@Transactional
class MessageServiceImpl(
	private val messageRepository: MessageRepository
) : MessageService {

	override fun send(userMsg: UserMsg): UserMsg {
		val userMsgEntity = userMsg.toUserMsgEntity(create = true)
		messageRepository.save(userMsgEntity)
		return userMsgEntity.toUserMsg()
	}

	override fun deleteById(messageId: Long): UserMsg {
		val userMsgEntity = messageRepository.findByIdOrNull(messageId) ?: throw MessageNotFoundException()
		val userMsg = userMsgEntity.toUserMsg()
		messageRepository.delete(userMsgEntity)
		return userMsg
	}

	override fun getByUser(userId: Long): List<UserMsg> {
		return messageRepository.findByToId(toId = userId).map { it.toUserMsg() }
	}

	override fun getById(messageId: Long): UserMsg {
		val userMsgEntity = messageRepository.findByIdOrNull(messageId) ?: throw MessageNotFoundException()
		return userMsgEntity.toUserMsg()
	}

	override fun setReadState(messageId: Long, readState: Boolean): UserMsg {
		val userMsgEntity = messageRepository.findByIdOrNull(messageId) ?: throw MessageNotFoundException()
		userMsgEntity.read = readState
		return userMsgEntity.toUserMsg()
	}

}