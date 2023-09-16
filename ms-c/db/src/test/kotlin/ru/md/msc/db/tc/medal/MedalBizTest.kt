package ru.md.msc.db.tc.medal

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.tc.TestBeans
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.medal.biz.proc.MedalCommand
import ru.md.msc.domain.medal.biz.proc.MedalContext
import ru.md.msc.domain.medal.biz.proc.MedalProcessor
import ru.md.msc.domain.medal.model.Medal
import ru.md.msc.domain.medal.model.MedalDetails
import ru.md.msc.domain.medal.service.MedalService

@SpringBootTest(classes = [TestBeans::class])
class MedalBizTest(
	@Autowired private val medalProcessor: MedalProcessor,
	@Autowired private val medalService: MedalService
) {

	@Test
	fun createMedalTest() = runBlocking {
		val context = MedalContext().apply {
			medal = Medal(name = "test", score = 50, dept = Dept(id = 3))
			medalDetails = MedalDetails(
				medal = medal,
				description = "desc"
			)
			command = MedalCommand.CREATE
		}

		medalProcessor.exec(context)

//		val medal = medalService.findMedalDetailsById(
//			medalId = context.medalDetails.medal.id
//		)

		println(context.errors)
		println(context.medalDetails)
	}

}