package ru.md.msc.domain.event.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.msc.domain.base.validate.auth.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.auth.validateAuthDeptLevel
import ru.md.msc.domain.base.validate.auth.validateAuthDeptTopLevelForView
import ru.md.msc.domain.base.validate.auth.validateAuthUserTopLevelForView
import ru.md.msc.domain.base.validate.validateDeptId
import ru.md.msc.domain.base.validate.validateUserId
import ru.md.msc.domain.base.workers.chain.validateSameAndAdminModifyUser
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.event.biz.validate.validateEventId
import ru.md.msc.domain.event.biz.workers.*
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

		private val businessChain = rootChain<EventsContext> {
			initStatus()

			operation("Добавить событие сотрудника", EventCommand.ADD_USER_EVENT) {
				validateUserId("Проверка userId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateSameAndAdminModifyUser() // Проверка модификации собственного профиля или Администратором
				addUserEvent("Добавляем событие сотрудника")
			}

			operation("Добавить событие отдела", EventCommand.ADD_DEPT_EVENT) {
				validateDeptId("Проверка deptId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptLevel("Проверка доступа к отделу")
				addDeptEvent("Добавляем событие отдела")
			}

			operation("Получить события", EventCommand.GET_ALL_EVENTS) {
				validateDeptId("Проверка deptId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				worker("Очищаем список сортировки") { baseQuery = baseQuery.copy(orders = emptyList()) }
				getAllEvents("Получаем события")
			}

			operation("Получить события сотрудника", EventCommand.GET_USER_EVENTS) {
				validateUserId("Проверка userId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthUserTopLevelForView("Проверка доступа к чтению данных сотрудника")
				getUserEvents("Получаем события сотрудника")
			}

			operation("Получить события отдела", EventCommand.GET_DEPT_EVENTS) {
				validateDeptId("Проверка deptId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getDeptEvents("Получаем события отдела")
			}

			operation("Удалить событие сотрудника", EventCommand.DELETE_USER_EVENT) {
				validateEventId("Проверка eventId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getUserEventById("Получаем событие сотрудника по Id")
				worker("Подготовка userId") { userId = baseEvent.userId }
				validateUserId("Проверка userId")
				validateSameAndAdminModifyUser() // Проверка модификации собственного профиля или Администратором
				deleteUserEvent("Удаляем событие сотрудника")
			}

			operation("Удалить событие отдела", EventCommand.DELETE_DEPT_EVENT) {
				validateEventId("Проверка eventId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getDeptEventById("Получаем событие сотрудника по Id")
				worker("Подготовка deptId") { deptId = baseEvent.deptId }
				validateDeptId("Проверка deptId")
				validateAuthDeptLevel("Проверка доступа к отделу")
				deleteDeptEvent("Удаляем событие отдела")
			}

			finishOperation()
		}.build()

	}
}