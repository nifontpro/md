package ru.md.msc.db.award.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.award.model.AwardEntity
import ru.md.msc.domain.award.model.AwardState
import java.time.LocalDateTime

@Repository
interface AwardRepository : JpaRepository<AwardEntity, Long> {

	@Query("select a.dept.id from AwardEntity a where a.id = :awardId")
	fun findDeptId(awardId: Long): Long?

	@Modifying
	@Query("delete from AwardEntity a where a.id = :awardId")
	override fun deleteById(awardId: Long)

	// ((:state is null) or (:state = award_state(a.startDate, a.endDate)))
	@EntityGraph("awardWithDept")
	@Query(
		"""
		from AwardEntity a where 
		a.dept.id = :deptId and 
		((:name is null) or (upper(a.name) like upper(:name))) and 
		((:state is null) or (:state = a.state))
		"""
	)
	fun findByDeptId(
		deptId: Long,
		name: String? = null,
		state: AwardState? = null,
		pageable: Pageable
	): Page<AwardEntity>

	@EntityGraph("awardWithDept")
	@Query(
		"""
		from AwardEntity a where 
		a.dept.id in :deptsIds and 
		((
			a.type = 'P'  and 
			a.startDate <= NOW() and (coalesce(:minDate, null) is null or a.startDate >= :minDate) and
			a.endDate >= NOW() and (coalesce(:maxDate, null) is null or a.endDate <= :maxDate)
		) or (
			a.type = 'S' and
			(coalesce(:minDate, null) is null or a.startDate >= :minDate) and 
			(coalesce(:maxDate, null) is null or a.endDate <= :maxDate)
		)) and 
		((:filter is null) or (upper(a.name) like upper(:filter)))
		
	"""
	)
	fun findByDeptIdIn(
		deptsIds: List<Long>,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
		filter: String? = null,
		pageable: Pageable
	): Page<AwardEntity>

	fun countByDeptId(deptId: Long): Long

	fun countByDeptIdIn(deptsIds: List<Long>): Long

}