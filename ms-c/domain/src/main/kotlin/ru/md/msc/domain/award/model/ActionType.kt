package ru.md.msc.domain.award.model

@Suppress("unused")
enum class ActionType(val code: String, val message: String) {
	NOMINEE("P", "Номинация сотрудника на награду"),
	AWARD("A", "Присвоение награды сотруднику"),
	DELETE("D", "Снятие награды с сотрудника"),
	UNDEF("N", "Нет")
}