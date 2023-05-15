package ru.md.base_domain.biz.helper

data class ContextError(
	val code: String = "",
	val group: String = "",
	val field: String = "",
	val message: String = "",
	val level: Levels = Levels.ERROR,
) {
	@Suppress("unused")
	enum class Levels {
		ERROR,
		WARNING,
		INFO,
		UNAUTHORIZED
	}
}