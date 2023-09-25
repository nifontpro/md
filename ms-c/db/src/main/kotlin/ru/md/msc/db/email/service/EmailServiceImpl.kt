package ru.md.msc.db.email.service

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.md.msc.domain.email.EmailService

@Service
class EmailServiceImpl(
	@Value("\${mail.username}") private val fromEmail: String,
	@Value("\${mail.host}") private val host: String,
	@Value("\${mail.port}") private val port: String,
	@Value("\${mail.password}") private val password: String,
): EmailService {

	override fun sendMail(toEmail: String, message: String) {
		SimpleEmail().apply {
			hostName = host
			setSmtpPort(port.toInt())
			setAuthenticator(DefaultAuthenticator(fromEmail, password))
			isSSLOnConnect = true
			setFrom(fromEmail)
			subject = "Медалист"
			setMsg(message)
			addTo(toEmail)
			send()
		}
	}

}

