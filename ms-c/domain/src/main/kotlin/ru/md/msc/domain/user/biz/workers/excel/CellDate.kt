package ru.md.msc.domain.user.biz.workers.excel

import java.time.LocalDateTime

internal data class CellDate(
	val text: String = "",
	val field: String = "",
	val date: LocalDateTime? = null,
	val success: Boolean = true
)