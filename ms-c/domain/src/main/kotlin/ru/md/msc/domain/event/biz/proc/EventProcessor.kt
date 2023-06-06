package ru.md.msc.domain.event.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.cor.rootChain
import ru.md.msc.domain.base.validate.db.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.validateUserId
import ru.md.msc.domain.base.workers.chain.validateSameAndAdminModifyUser
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.event.biz.workers.addUserEvent
import ru.md.msc.domain.event.service.EventService
import ru.md.msc.domain.user.service.UserService

@Component
class EventProcessor(
	private val userService: UserService,
	private val deptService: DeptService,
	private val eventService: EventService
) : IBaseProcessor<EventsContext> {

	override suspend fun exec(ctx: EventsContext) = businessChain.exec(ctx.also {
		it.userService = userService
		it.deptService = deptService
		it.eventService = eventService
	})

	companion object {

		private val businessChain = rootChain {
			initStatus()

			operation("Добавить событие сотрудника", EventCommand.ADD_USER_EVENT) {
				validateUserId("Проверка userId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateSameAndAdminModifyUser() // Проверка модификации собственного профиля или Администратором
				addUserEvent("Добавляем событие сотрудника")
			}

			operation("Добавить событие отдела", EventCommand.ADD_DEPT_EVENT) {

			}


			finishOperation()
		}.build()

	}
}