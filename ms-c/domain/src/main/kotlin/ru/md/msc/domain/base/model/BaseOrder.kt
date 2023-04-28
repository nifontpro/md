package ru.md.msc.domain.base.model

data class BaseOrder(
	val field: String,
	val direction: Direction = Direction.ASC,
)

enum class Direction {
	ASC,
	DESC
}
