package ru.md.msc.rest.message.model.request

data class SendMessageRequest(
	val authId: Long = 0,
	val userId: Long = 0,
	val msg: String? = null
)