package ru.md.msc.domain.msg.service

import ru.md.msc.domain.msg.model.UserMsg

interface MessageService {
	fun send(userMsg: UserMsg): UserMsg
	fun deleteById(messageId: Long): UserMsg
	fun getByUser(userId: Long): List<UserMsg>
	fun getById(messageId: Long): UserMsg
	fun setReadState(messageId: Long, readState: Boolean): UserMsg
}