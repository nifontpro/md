package ru.md.msc.domain.medal

import ru.md.msc.domain.dept.model.Dept

data class Medal(
	val id: Long = 0,
	val name: String = "",
	val mainImg: String? = null,
	val score: Int = 0,
	val dept: Dept? = null,
)