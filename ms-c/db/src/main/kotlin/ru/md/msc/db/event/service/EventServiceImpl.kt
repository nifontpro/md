package ru.md.msc.db.event.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.md.msc.db.event.model.DeptEventEntity
import ru.md.msc.db.event.model.UserEventEntity
import ru.md.msc.db.event.model.mappers.toBaseEvent
import ru.md.msc.db.event.repo.DeptEventRepository
import ru.md.msc.db.event.repo.UserEventRepository
import ru.md.msc.domain.event.model.BaseEvent
import ru.md.msc.domain.event.service.EventService

@Service
@Transactional
class EventServiceImpl(
	private val userEventRepository: UserEventRepository,
	private val deptEventRepository: DeptEventRepository
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

	override fun addDeptEvent(deptId: Long, baseEvent: BaseEvent): BaseEvent {
		val deptEventEntity = DeptEventEntity(
			eventDate = baseEvent.eventDate,
			eventName = baseEvent.eventName,
			deptId = deptId,
		)
		deptEventRepository.save(deptEventEntity)
		return deptEventEntity.toBaseEvent()
	}

}