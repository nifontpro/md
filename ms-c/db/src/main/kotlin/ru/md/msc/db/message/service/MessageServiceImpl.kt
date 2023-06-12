package ru.md.msc.db.message.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.msc.db.message.model.mappers.toUserMsg
import ru.md.msc.db.message.model.mappers.toUserMsgEntity
import ru.md.msc.db.message.repo.MessageRepository
import ru.md.msc.domain.message.biz.proc.MessageNotFoundException
import ru.md.msc.domain.message.model.UserMsg
import ru.md.msc.domain.message.service.MessageService

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