package ru.md.base.base_db.mapper

import org.springframework.data.domain.Sort

fun BaseOrder.toOrder() = Sort.Order(
	when (direction) {
		Direction.ASC -> Sort.Direction.ASC
		Direction.DESC -> Sort.Direction.DESC
	},
	field
)

fun List<BaseOrder>.toSort(): Sort = Sort.by(this.map { it.toOrder() })