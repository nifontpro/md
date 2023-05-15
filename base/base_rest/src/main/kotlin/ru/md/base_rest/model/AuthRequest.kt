package ru.md.base_rest.model

const val AUTH = "Authorization"

data class AuthRequest<out T>(
	val data: T,
	val authEmail: String? = null,
	val emailVerified: Boolean = false
)
