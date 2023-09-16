package ru.md.msc.db.tc.medal

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
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

const val ownerEmail = "owner@test.ru"

@SpringBootTest(classes = [TestBeans::class])
class MedalBizTest(
	@Autowired private val medalProcessor: MedalProcessor,
	@Autowired private val medalService: MedalService
) {

	@Test
	fun createAndGetMedalTest() = runBlocking {

		val name = "test"
		val description = "desc"
		val score = 50
		val deptId = 3L

		val createContext = MedalContext().apply {
			authId = 1
			authEmail = ownerEmail
			medal = Medal(name = name, score = score, dept = Dept(id = deptId))
			medalDetails = MedalDetails(
				medal = medal,
				description = description
			)
			command = MedalCommand.CREATE
		}

		medalProcessor.exec(createContext)

//		val medal = medalService.findMedalDetailsById(
//			medalId = context.medalDetails.medal.id
//		)

		println("Errors: ${createContext.errors}")
		println(createContext.medalDetails)

		// Получение:
		val getContext = MedalContext().apply {
			authId = 1
			authEmail = ownerEmail
			medalId = createContext.medalDetails.medal.id
			command = MedalCommand.GET_BY_ID_DETAILS
		}
		medalProcessor.exec(getContext)

		println(createContext.medalDetails)
		assertEquals(createContext.medalDetails.medal, getContext.medalDetails.medal)
		assertEquals(createContext.medalDetails.description, getContext.medalDetails.description)
	}

}