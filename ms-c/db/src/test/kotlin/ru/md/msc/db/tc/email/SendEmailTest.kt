package ru.md.msc.db.tc.email

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.tc.TestBeans

@SpringBootTest(classes = [TestBeans::class])
class SendEmailTest
//	(
//	@Autowired private val emailService: EmailService,
//)
{

	@Test
	fun sendEmailTest() {
//		emailService.sendMail("nifontbus@yandex.ru", "Test message")
//		println("Email: OK")
	}

}