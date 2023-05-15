package ru.md.base_domain.model

import java.time.LocalDateTime
import java.util.Collections.emptyList

data class BaseQuery(
    val page: Int = 0,
    val pageSize: Int = DEFAULT_PAGE_SIZE,
    val filter: String? = null,
    val minDate: LocalDateTime? = null,
    val maxDate: LocalDateTime? = null,
    val orders: List<BaseOrder> = emptyList(),
)