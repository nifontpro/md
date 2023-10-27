package ru.md.base_db.base.mapper

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.JpaSort
import ru.md.base_domain.model.BaseOrder
import ru.md.base_domain.model.Direction

//fun BaseOrder.toOrder(): Sort.Order = Sort.Order(
//	when (direction) {
//		Direction.ASC -> Sort.Direction.ASC
//		Direction.DESC -> Sort.Direction.DESC
//	},
//	field
//)

//fun List<BaseOrder>.toSort(): Sort = Sort.by(this.map { it.toOrder() })

fun getDirect(direction: Direction) = when (direction) {
	Direction.ASC -> Sort.Direction.ASC
	Direction.DESC -> Sort.Direction.DESC
}

fun List<BaseOrder>.toSort(): Sort {
	var sort: Sort = Sort.by(emptyList())
	for (order in this) {
		sort = if (order.field.matches("^\\(.*\\)$".toRegex())) {
			sort.and(
				JpaSort.unsafe(
					getDirect(order.direction),
					order.field
				)
			)
		} else {
			sort.and(Sort.by(getDirect(order.direction), order.field))
		}
	}
	return sort
}