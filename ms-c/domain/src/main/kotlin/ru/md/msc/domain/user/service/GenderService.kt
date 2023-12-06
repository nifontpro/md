package ru.md.msc.domain.user.service

interface GenderService {
	fun addMaleName(name: String)
	fun addFemaleName(name: String)
	fun addMaleLastname(name: String)
}