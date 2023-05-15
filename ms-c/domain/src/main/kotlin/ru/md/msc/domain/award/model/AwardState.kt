package ru.md.msc.domain.award.model

@Suppress("unused")
enum class AwardState(val code: String) {
	FUTURE("FUTURE"),
	NOMINEE("NOMINEE"),
	FINISH("FINISH"),
	ERROR("ERROR")
}