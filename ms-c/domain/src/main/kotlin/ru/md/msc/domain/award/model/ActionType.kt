package ru.md.msc.domain.award.model

enum class ActionType(val code: String, val message: String) {
	NOMINEE("P", "Номинация сотрудника на награду"),
	AWARD("A", "Присвоение награды сотруднику"),
	DELETE("D", "Снятие награды с сотрудника"),
	UNDEF("N", "Нет")
}


fun ActionType.actionMessage(): String {
	return when (this) {
		ActionType.NOMINEE -> "Поздравляем! Вы номинированы на награду"
		ActionType.AWARD -> "Поздравляем! Вы награждены наградой"
		ActionType.DELETE -> "С Вас снято награждение"
		ActionType.UNDEF -> ""
	}
}