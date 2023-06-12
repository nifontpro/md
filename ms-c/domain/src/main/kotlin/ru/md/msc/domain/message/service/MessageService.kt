package ru.md.msc.domain.message.service

import ru.md.msc.domain.message.model.UserMsg

interface MessageService {
	fun send(userMsg: UserMsg): UserMsg
	fun deleteById(messageId: Long): UserMsg
	fun getByUser(userId: Long): List<UserMsg>
	fun getById(messageId: Long): UserMsg
	fun setReadState(messageId: Long, readState: Boolean): UserMsg
}