package ru.md.msc.rest.message.model.request

data class SetMessageReadStatusRequest(
	val authId: Long = 0,
	val messageId: Long = 0,
	val read: Boolean = true
)