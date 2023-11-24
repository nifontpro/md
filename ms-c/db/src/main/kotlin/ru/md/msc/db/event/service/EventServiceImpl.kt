package ru.md.msc.db.event.service

import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.base.mapper.toPageRequest
import ru.md.base_db.base.mapper.toPageResult
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.msc.db.dept.service.DeptUtil
import ru.md.msc.db.event.model.DeptEventEntity
import ru.md.msc.db.event.model.UserEventEntity
import ru.md.msc.db.event.model.mappers.toBaseEvent
import ru.md.msc.db.event.model.mappers.toShortEvent
import ru.md.msc.db.event.model.mappers.toUserEvent
import ru.md.msc.db.event.repo.DeptEventRepository
import ru.md.msc.db.event.repo.UserEventRepository
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.event.biz.proc.EventNotFoundException
import ru.md.msc.domain.event.model.BaseEvent
import ru.md.msc.domain.event.model.ShortEvent
import ru.md.msc.domain.event.model.UserEvent
import ru.md.msc.domain.event.service.EventService

@Service
@Transactional
class EventServiceImpl(
	private val userEventRepository: UserEventRepository,
	private val deptEventRepository: DeptEventRepository,
	private val deptUtil: DeptUtil,
) : EventService {

	override fun addUserEvent(userId: Long, baseEvent: BaseEvent): BaseEvent {
		val userEventEntity = UserEventEntity(
			eventDate = baseEvent.eventDate,
			eventName = baseEvent.eventName,
			userId = userId,
		)
		userEventRepository.save(userEventEntity)
		return userEventEntity.toBaseEvent()
	}

	override fun addUserEvent(userEvent: UserEvent): UserEvent {
		val userEventEntity = UserEventEntity(
			eventDate = userEvent.eventDate,
			eventName = userEvent.eventName,
			userId = userEvent.userId,
		)
		userEventRepository.save(userEventEntity)
		return userEventEntity.toUserEvent(isUpdate = false)
	}

	override fun addOrUpdateUserEvent(userEvent: UserEvent): UserEvent {
		val userEventEntity = userEventRepository.findByUserIdAndEventName(
			userId = userEvent.userId,
			eventName = userEvent.eventName
		) ?: run {
			val newUserEventEntity = UserEventEntity(
				eventDate = userEvent.eventDate,
				eventName = userEvent.eventName,
				userId = userEvent.userId,
			)
			userEventRepository.save(newUserEventEntity)
			return newUserEventEntity.toUserEvent(isUpdate = false)
		}

		val isUpdate = if (userEventEntity.eventDate != userEvent.eventDate) {
			userEventEntity.eventDate = userEvent.eventDate
			true
		} else {
			false
		}

		return userEventEntity.toUserEvent(isUpdate = isUpdate)
	}

	override fun addDeptEvent(deptId: Long, baseEvent: BaseEvent): BaseEvent {
		val deptEventEntity = DeptEventEntity(
			eventDate = baseEvent.eventDate,
			eventName = baseEvent.eventName,
			deptId = deptId,
		)
		deptEventRepository.save(deptEventEntity)
		return deptEventEntity.toBaseEvent()
	}

	override fun getEvents(deptId: Long, baseQuery: BaseQuery): PageResult<BaseEvent> {
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)
		return deptEventRepository.getEvents(deptsIds = deptsIds, pageable = baseQuery.toPageRequest())
			.toPageResult { it.toBaseEvent() }
	}

	override fun getEventsByUser(userId: Long): List<ShortEvent> {
		return userEventRepository.findByUserId(userId = userId)
			.map { it.toShortEvent() }
	}

	override fun getEventsByDept(deptId: Long): List<ShortEvent> {
		return deptEventRepository.findByDeptId(deptId = deptId)
			.map { it.toShortEvent() }
	}

	override fun getUserEventById(eventId: Long): BaseEvent {
		val eventEntity = userEventRepository.findByIdOrNull(eventId) ?: throw EventNotFoundException()
		return eventEntity.toBaseEvent()
	}

	override fun getDeptEventById(eventId: Long): BaseEvent {
		val eventEntity = deptEventRepository.findByIdOrNull(eventId) ?: throw EventNotFoundException()
		return eventEntity.toBaseEvent()
	}

	override fun deleteUserEventById(eventId: Long) {
		userEventRepository.deleteById(eventId)
	}

	override fun deleteDeptEventById(eventId: Long) {
		deptEventRepository.deleteById(eventId)
	}

	companion object {
		@Suppress("unused")
		val log: Logger = LoggerFactory.getLogger(BaseClientContext::class.java)
	}

}