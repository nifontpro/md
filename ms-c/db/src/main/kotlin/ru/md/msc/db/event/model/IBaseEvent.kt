package ru.md.msc.db.event.model

import java.time.LocalDateTime

interface IBaseEvent {
	fun getId(): Long
	fun getEventDate(): LocalDateTime
	fun getDays(): Int
	fun getEventName(): String
	fun getEntityName(): String
	fun getImageUrl(): String?
	fun getUserId(): Long
	fun getDeptId(): Long
	fun getDeptName(): String
	fun getDeptClassname(): String?
}