package ru.md.msc.db.user.model

interface IUser {
	fun getUserId(): Long
	fun getFirstname(): String
	fun getLastname(): String?
	fun getPatronymic(): String?
	fun getPost(): String?
	fun getMainImg(): String?
	fun getDeptId(): Long
	fun getDeptName(): String
	fun getClassname(): String?
	fun getAwardCount(): Long
}