package ru.md.msc.db.email.service

import org.apache.commons.mail.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.md.msc.domain.email.EmailService
import java.net.URL

const val ssl = true

@Service
class EmailServiceImpl(
	@Value("\${mail.username}") private val username: String,
	@Value("\${mail.host}") private val host: String,
	@Value("\${mail.port}") private val port: String,
	@Value("\${mail.password}") private val password: String,
	@Value("\${mail.from.email}") private val fromEmail: String,
	@Value("\${mail.from.name}") private val fromName: String,
	@Value("\${mail.from.subject}") private val fromSubject: String,
) : EmailService {

	override fun sendMail(toEmail: String, message: String) {
		SimpleEmail().apply {
			hostName = host
			setSmtpPort(port.toInt())
			setAuthenticator(DefaultAuthenticator(username, password))
			isSSLOnConnect = ssl
//			setStartTLSEnabled(true)
			setFrom(fromEmail, fromName)
			subject = fromSubject
			setMsg(message)
			addTo(toEmail)
			send()
		}
	}

	// https://betacode.net/10147/java-commons-email
	override fun send(toEmail: String, message: String) {
		// Create the attachment
		val attachment = EmailAttachment()
		attachment.url = URL(
			"http://www.apache.org/images/asf_logo_wide.gif"
		)
		attachment.disposition = EmailAttachment.INLINE
		attachment.description = "Apache logo"
		attachment.name = "Apache logo"

		// Create the email message
		val email = MultiPartEmail().apply {
			hostName = host
			setSmtpPort(port.toInt())
			setAuthenticator(DefaultAuthenticator(username, password))
			isSSLOnConnect = ssl
//			setStartTLSEnabled(true)
			setFrom(fromEmail, fromName)
			subject = fromSubject
			setMsg(message)
			addTo(toEmail)
		}

		email.attach(attachment)
		email.send()
	}

	override fun sendHtml(toEmail: String, message: String) {
		val email = HtmlEmail().apply {
			hostName = host
			setSmtpPort(port.toInt())
			setAuthenticator(DefaultAuthenticator(username, password))
			isSSLOnConnect = ssl
//			setStartTLSEnabled(true)
			setFrom(fromEmail, fromName)
			subject = fromSubject
			addTo(toEmail)
		}

		email.setCharset("UTF-8")
		email.setHtmlMsg(message)
		email.setTextMsg("Your email client does not support HTML messages")
		email.send()

	}

}

