package ru.md.msc.domain.award.model

enum class AwardState(val code: String) {
	FUTURE("FUTURE"),
	NOMINEE("NOMINEE"),
	FINISH("FINISH"),
	ERROR("ERROR")
}