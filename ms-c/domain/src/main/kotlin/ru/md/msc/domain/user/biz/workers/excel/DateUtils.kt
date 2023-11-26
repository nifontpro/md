package ru.md.msc.domain.user.biz.workers.excel

import org.dhatim.fastexcel.reader.Cell
import org.dhatim.fastexcel.reader.CellType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal fun String.isInt() = this.toIntOrNull()?.let { true } ?: false

internal fun String.toDate(): LocalDateTime {
	return LocalDate.parse(this.trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy")).atStartOfDay()
}

internal fun cellToDate(cell: Cell, field: String): CellDate {
	return when (cell.type) {
		CellType.NUMBER -> {
			try {
				val date = cell.asDate() ?: throw Exception()
				CellDate(
					text = "",
					date = date,
					success = true
				)
			} catch (e: Exception) {
				CellDate(
					text = cell.dataFormatString,
					date = null,
					success = false
				)
			}
		}

		CellType.STRING -> {
			try {
				val date = cell.text.toDate()
				CellDate(
					text = cell.text,
					date = date,
					success = true
				)
			} catch (e: Exception) {
				CellDate(
					text = cell.text,
					date = null,
					success = false
				)
			}
		}

		else -> CellDate(
			date = null,
			success = false
		)
	}.copy(field = field)
}