package ru.md.msc.db.event.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.md.base_db.mapper.toPageRequest
import ru.md.base_db.mapper.toPageResult
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.msc.db.dept.repo.DeptRepository
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
	private val deptEventRepository: DeptEventRepository,
	private val deptRepository: DeptRepository,
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

	override fun getEvents(deptId: Long, baseQuery: BaseQuery): PageResult<BaseEvent> {
		println("subdepts: ${baseQuery.subdepts}")
		val deptsIds = getDepts(deptId = deptId, subdepts = baseQuery.subdepts)
		return deptEventRepository.getEvents(deptsIds = deptsIds, pageable = baseQuery.toPageRequest())
			.toPageResult { it.toBaseEvent() }
	}

	/**
	 * Получение списка отделов от текущей вершины дерева
	 * subdepts = true - все дерево подотделов
	 * subdepts = false:
	 *    nearSub = false (default) - только вершина
	 *            = true - непосредственные потомки
	 */
	private fun getDepts(deptId: Long, subdepts: Boolean, nearSub: Boolean = false): List<Long> {
		return if (subdepts) {
			deptRepository.subTreeIds(deptId = deptId)
		} else {
			if (nearSub) {
				deptRepository.findByParentId(parentId = deptId)
			} else {
				listOf(deptId)
			}
		}
	}

}