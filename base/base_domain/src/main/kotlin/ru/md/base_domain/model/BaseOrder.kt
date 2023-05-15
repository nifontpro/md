package ru.md.base_domain.model

data class BaseOrder(
    val field: String,
    val direction: Direction = Direction.ASC,
)

enum class Direction {
	ASC,
	DESC
}
