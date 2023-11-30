package ru.md.base_rest.utils

fun testResponse(): String {
	val usedMb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576
	return "Test: Ok. URL: " + System.getenv("PREFIX_URL") + ", used: $usedMb Mb"
}