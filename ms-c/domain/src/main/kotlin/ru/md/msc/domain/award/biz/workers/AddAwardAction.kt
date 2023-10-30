package ru.md.msc.domain.award.biz.workers

import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.proc.AlreadyActionException
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.model.ActionType
import ru.md.msc.domain.award.model.Activity
import java.time.LocalDateTime

fun ICorChainDsl<AwardContext>.addAwardAction(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		val newActivity = Activity(
			user = user,
//			user = User(id = userId),
			award = award,
			date = LocalDateTime.now(),
			actionType = actionType,
			activ = actionType == ActionType.NOMINEE || actionType == ActionType.AWARD,
			dept = user.dept,
			authId = authId,
			awardScore = if (actionType == ActionType.AWARD) award.score else 0,
		)
		activity = awardService.sendActivity(activity = newActivity)
	}

	except {
		log.error(it.message)
		when (it) {
			is AlreadyActionException -> fail(
				errorDb(
					repository = "award",
					violationCode = "already action",
					description = "Повторное действие недопустимо: ${actionType.message}",
					level = ContextError.Levels.INFO,
				)
			)

			else -> {
				fail(
					errorDb(
						repository = "award",
						violationCode = actionType.name.lowercase(),
						description = "Ошибка:" + actionType.message
					)
				)
			}
		}
	}

}