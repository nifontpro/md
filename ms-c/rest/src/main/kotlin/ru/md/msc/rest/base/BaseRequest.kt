package ru.md.msc.rest.base

const val AUTH = "Authorization"

data class BaseRequest<out T>(
	val data: T,
	val authEmail: String? = null,
	val emailVerified: Boolean = false
)
