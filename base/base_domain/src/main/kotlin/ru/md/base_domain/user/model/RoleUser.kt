package ru.md.base_domain.user.model

enum class RoleUser(val code: String) {
	USER("U"),
	ADMIN("A"),
	OWNER("O"),
}