package ru.md.base_db.mapper

import org.springframework.data.domain.Sort
import ru.md.base_domain.model.BaseOrder
import ru.md.base_domain.model.Direction

fun BaseOrder.toOrder() = Sort.Order(
    when (direction) {
        Direction.ASC -> Sort.Direction.ASC
        Direction.DESC -> Sort.Direction.DESC
    },
    field
)

fun List<BaseOrder>.toSort(): Sort = Sort.by(this.map { it.toOrder() })