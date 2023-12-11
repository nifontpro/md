package ru.md.msc.db.tc.user

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.tc.TestBeans
import ru.md.msc.db.tc.ownerEmail
import ru.md.msc.domain.user.biz.proc.UserCommand
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.UserProcessor

@SpringBootTest(classes = [TestBeans::class])

class ExcelTest(
	@Autowired private val userProcessor: UserProcessor,
) {

	@Test
	fun formatExcelTest() = runBlocking {
		var userContext = UserContext().apply {
			authId = 3
			authEmail = ownerEmail
			command = UserCommand.ADD_FROM_EXCEL
			fileUrl = "\$MD_APP/sql/users.xlsx"
		}
		userProcessor.exec(userContext)

		userContext = UserContext().apply {
			authId = 3
			authEmail = ownerEmail
			command = UserCommand.ADD_FROM_EXCEL
			fileUrl = "\$MD_APP/sql/users.xlsx"
		}
		userProcessor.exec(userContext)
	}

}