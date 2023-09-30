package ru.md.msc.domain.email

interface EmailService {
	fun sendMail(toEmail: String, message: String)
	fun send(toEmail: String, message: String)
	fun sendHtml(toEmail: String, message: String)
}