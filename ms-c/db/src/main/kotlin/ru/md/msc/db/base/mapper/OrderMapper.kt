package ru.md.msc.db.base.mapper

import org.springframework.data.domain.Sort
import ru.md.msc.domain.base.model.BaseOrder
import ru.md.msc.domain.base.model.Direction

fun BaseOrder.toOrder() = Sort.Order(
	when (direction) {
		Direction.ASC -> Sort.Direction.ASC
		Direction.DESC -> Sort.Direction.DESC
	},
	field
)