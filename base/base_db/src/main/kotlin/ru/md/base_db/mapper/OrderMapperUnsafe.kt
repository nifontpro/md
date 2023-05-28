package ru.md.base_db.mapper

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.JpaSort
import ru.md.base_domain.model.BaseOrder
import ru.md.base_domain.model.Direction


fun BaseOrder.toOrderUnsafe() = JpaSort.unsafe(
	when (direction) {
		Direction.ASC -> Sort.Direction.ASC
		Direction.DESC -> Sort.Direction.DESC
	},
	field, ""
)