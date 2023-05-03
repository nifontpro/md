package ru.md.msc.domain.award.model

enum class AwardType(val code: String) {
	PERIOD("P"),
	SIMPLE("S"),
	UNDEF("N")
}