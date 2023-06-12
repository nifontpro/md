package ru.md.msc.rest.message.model.request

data class DeleteMessageRequest(
	val authId: Long = 0,
	val messageId: Long = 0,
)