package ru.md.award.db.medal

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.award.db.TestBeans
import ru.md.award.db.ownerEmail
import ru.md.award.domain.medal.biz.proc.MedalCommand
import ru.md.award.domain.medal.biz.proc.MedalContext
import ru.md.award.domain.medal.biz.proc.MedalProcessor
import ru.md.award.domain.medal.model.Medal
import ru.md.award.domain.medal.model.MedalDetails
import ru.md.base_domain.dept.model.Dept

@SpringBootTest(classes = [TestBeans::class])
class CrudMedal(
	@Autowired private val medalProcessor: MedalProcessor,
) {

	@Test
	fun crudMedalTest() = runBlocking {

		var name = "test"
		var description = "desc"
		var criteria = "criteria"
		var score = 50
		val depId = 3L

		val createContext = MedalContext().apply {
			authId = 1
			deptId = depId
			authEmail = ownerEmail
			medal = Medal(name = name, score = score, dept = Dept(id = depId))
			medalDetails = MedalDetails(
				medal = medal,
				description = description,
				criteria = criteria
			)
			command = MedalCommand.CREATE
		}

		medalProcessor.exec(createContext)

//		val medal = medalService.findMedalDetailsById(
//			medalId = context.medalDetails.medal.id
//		)

		println("Errors: ${createContext.errors}")
		println(createContext.medalDetails)
		assertEquals(0, createContext.errors.count())

		// Получение:
		var getContext = MedalContext().apply {
			authId = 1
			authEmail = ownerEmail
			medalId = createContext.medalDetails.medal.id
			command = MedalCommand.GET_BY_ID
		}
		medalProcessor.exec(getContext)

		println("Errors: ${getContext.errors}")
		assertEquals(0, getContext.errors.count())
		println(createContext.medalDetails)
		assertEquals(name, getContext.medalDetails.medal.name)
		assertEquals(score, getContext.medalDetails.medal.score)
		assertEquals(depId, getContext.medalDetails.medal.dept.id)
		assertEquals(description, getContext.medalDetails.description)
		assertEquals(criteria, getContext.medalDetails.criteria)
		assertEquals(createContext.medalDetails.medal.id, getContext.medalDetails.medal.id)

		// Update
		name = "update test"
		description = "update desc"
		criteria = "update criteria"
		score = 500

		val updateContext = MedalContext().apply {
			authId = 1
			authEmail = ownerEmail
			medalId = createContext.medalDetails.medal.id
			medal = Medal(id = medalId, name = name, score = score)
			medalDetails = MedalDetails(
				medal = medal,
				description = description,
				criteria = criteria
			)
			command = MedalCommand.UPDATE
		}
		medalProcessor.exec(updateContext)
		println("Update Errors: ${updateContext.errors}")
		assertEquals(0, updateContext.errors.count())
		println(updateContext.medalDetails)

		getContext = MedalContext().apply {
			authId = 1
			authEmail = ownerEmail
			medalId = createContext.medalDetails.medal.id
			command = MedalCommand.GET_BY_ID
		}
		medalProcessor.exec(getContext)

		println("Errors: ${getContext.errors}")
		assertEquals(0, getContext.errors.count())
		println(getContext.medalDetails)
		assertEquals(createContext.medalDetails.medal.id, getContext.medalDetails.medal.id)
		assertEquals(name, getContext.medalDetails.medal.name)
		assertEquals(score, getContext.medalDetails.medal.score)
		assertEquals(depId, getContext.medalDetails.medal.dept.id)
		assertEquals(description, getContext.medalDetails.description)
		assertEquals(criteria, getContext.medalDetails.criteria)

		// Delete
		val deleteContext = MedalContext().apply {
			authId = 1
			authEmail = ownerEmail
			medalId = createContext.medalDetails.medal.id
			command = MedalCommand.DELETE
		}
		medalProcessor.exec(deleteContext)
		println("Delete Errors: ${deleteContext.errors}")
		assertEquals(0, deleteContext.errors.count())
		assertEquals(createContext.medalDetails.medal.id, deleteContext.medalDetails.medal.id)

		getContext = MedalContext().apply {
			authId = 1
			authEmail = ownerEmail
			medalId = createContext.medalDetails.medal.id
			command = MedalCommand.GET_BY_ID
		}
		medalProcessor.exec(getContext)

		println("Errors: ${getContext.errors}")
		assertEquals(1, getContext.errors.count())
		assertEquals("db-medal:not found", getContext.errors.first().code)

	}

}