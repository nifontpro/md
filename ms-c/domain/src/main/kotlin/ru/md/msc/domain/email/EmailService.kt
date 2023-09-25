package ru.md.msc.domain.email

interface EmailService {
	fun sendMail(toEmail: String, message: String)
}