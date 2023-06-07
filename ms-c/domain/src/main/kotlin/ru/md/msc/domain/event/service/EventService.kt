package ru.md.msc.domain.event.service

import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.msc.domain.event.model.BaseEvent
import ru.md.msc.domain.event.model.ShortEvent

interface EventService {
	fun addUserEvent(userId: Long, baseEvent: BaseEvent): BaseEvent
	fun addDeptEvent(deptId: Long, baseEvent: BaseEvent): BaseEvent
	fun getEvents(deptId: Long, baseQuery: BaseQuery): PageResult<BaseEvent>
	fun getEventsByUser(userId: Long): List<ShortEvent>
	fun getEventsByDept(deptId: Long): List<ShortEvent>
}