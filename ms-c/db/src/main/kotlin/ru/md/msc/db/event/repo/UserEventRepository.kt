package ru.md.msc.db.event.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.event.model.IShortEvent
import ru.md.msc.db.event.model.UserEventEntity

@Repository
interface UserEventRepository : JpaRepository<UserEventEntity, Long> {

	@Query(
		"""
		select 
			e.id,
			e.event_date eventDate,
			(extract(DOY FROM e.event_date)) days,
			e.name eventName
		 from env.user_event e where e.user_id=:userId
		 order by days, eventName
	""",
		nativeQuery = true
	)
	fun findByUserId(userId: Long): List<IShortEvent>

	fun findByUserIdAndEventName(
		userId: Long,
		eventName: String
	): UserEventEntity?

}