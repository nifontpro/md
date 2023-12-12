package ru.md.msc.domain.event.service

import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.msc.domain.event.model.BaseEvent
import ru.md.msc.domain.event.model.ShortEvent
import ru.md.msc.domain.event.model.UserEvent

interface EventService {
	fun addUserEvent(userId: Long, baseEvent: BaseEvent): BaseEvent
	fun addDeptEvent(deptId: Long, baseEvent: BaseEvent): BaseEvent
	fun getEvents(deptId: Long, baseQuery: BaseQuery): PageResult<BaseEvent>
	fun getEventsByUser(userId: Long): List<ShortEvent>
	fun getEventsByDept(deptId: Long): List<ShortEvent>
	fun deleteUserEventById(eventId: Long)
	fun getUserEventById(eventId: Long): BaseEvent
	fun getDeptEventById(eventId: Long): BaseEvent
	fun deleteDeptEventById(eventId: Long)
	fun addOrUpdateUserEvent(userEvent: UserEvent): UserEvent
	fun addUserEvent(userEvent: UserEvent): UserEvent
	fun deleteByUserIdAndEventName(userId: Long, eventName: String)
}