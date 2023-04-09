package ru.md.cor.base.helper

data class ContextError(
	val code: String = "",
	val group: String = "",
	val field: String = "",
	val message: String = "",
	val exception: Throwable? = null,
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

// ответ API
data class BaseError(
	val errors: List<ContextError>,
	val type: String = "multiError"
)