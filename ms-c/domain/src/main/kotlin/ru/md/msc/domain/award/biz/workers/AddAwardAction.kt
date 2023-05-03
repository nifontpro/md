package ru.md.msc.domain.award.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AlreadyActionException
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.Activity
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.ContextError
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.user.model.User
import java.time.LocalDateTime

fun ICorChainDsl<AwardContext>.addAwardAction(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		val newActivity = Activity(
			user = User(id = userId),
			award = award,
			date = LocalDateTime.now(),
			actionType = actionType,
			activ = true
		)

		try {
			activity = awardService.awardUser(activity = newActivity)
		} catch (e: AlreadyActionException) {
			fail(
				errorDb(
					repository = "award",
					violationCode = "already action",
					description = "Повторное действие недопустимо: ${actionType.message}",
					level = ContextError.Levels.INFO,
				)
			)
		} catch (e: Exception) {
			log.error(e.message)
			fail(
				errorDb(
					repository = "award",
					violationCode = "award user",
					description = "Ошибка награждения сотрудника"
				)
			)
		}
	}
}