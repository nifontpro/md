package ru.md.msc.domain.medal.model

import ru.md.base_domain.dept.model.Dept

data class Medal(
	val id: Long = 0,
	val name: String = "",
	val mainImg: String? = null,
	val score: Int = 0,
	val dept: Dept? = null,
)