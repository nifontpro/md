package ru.md.shop.domain.pay.model

enum class PayCode(val code: String) {
	PAY("P"),
	GIVEN("G"),
	RETURN("R"),
	UNDEF("N")
}