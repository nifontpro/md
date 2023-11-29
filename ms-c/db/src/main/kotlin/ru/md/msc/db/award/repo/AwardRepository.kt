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
import ru.md.msc.domain.award.model.AwardStateCount
import java.time.LocalDateTime

@Repository
interface AwardRepository : JpaRepository<AwardEntity, Long> {

	@Query("select a.dept.id from AwardEntity a where a.id = :awardId")
	fun findDeptId(awardId: Long): Long?

	@Modifying
	@Query("delete from AwardEntity a where a.id = :awardId")
	override fun deleteById(awardId: Long)

	@EntityGraph("awardWithDept")
	@Query(
		"""
		from AwardEntity a where 
		a.dept.id in :deptsIds and 
		((:state is null) or (:state = a.state)) and
		((:notExclude = true) or (a.id not in :excludeAwardIds)) and 
		((
			a.type = 'P'  and 
			a.startDate <= NOW() and (:minDateNull = true or a.startDate >= :minDate) and
			a.endDate >= NOW() and (:maxDateNull = true or a.endDate <= :maxDate)
		) or (
			a.type = 'S' and
			(:minDateNull = true or a.startDate >= :minDate) and 
			(:maxDateNull = true or a.endDate <= :maxDate)
		)) and 
		(:filter is null or (upper(a.name) like :filter))
		
	"""
	)
	fun findByDeptIdIn(
		deptsIds: List<Long>,
		state: AwardState? = null,
		minDateNull: Boolean,
		maxDateNull: Boolean,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
		filter: String? = null,
		notExclude: Boolean = true,
		excludeAwardIds: List<Long> = emptyList(),
		pageable: Pageable
	): Page<AwardEntity>

	@EntityGraph("awardWithDeptAndUser")
	@Query(
		"""
		from AwardEntity a where 
		a.dept.id in :deptsIds and 
		((:state is null) or (:state = a.state and
		(:state != 'FINISH' or (:state = 'FINISH' and a.userCount>0)))) and 
		((
			a.type = 'P'  and 
			a.startDate <= NOW() and (:minDateNull = true or a.startDate >= :minDate) and
			a.endDate >= NOW() and (:maxDateNull = true or a.endDate <= :maxDate)
		) or (
			a.type = 'S' and
			(:minDateNull = true or a.startDate >= :minDate) and 
			(:maxDateNull = true or a.endDate <= :maxDate)
		)) and 
		((:filter is null) or (upper(a.name) like :filter))
		
	"""
	)
	fun findByDeptIdInWithUsers(
		deptsIds: List<Long>,
		state: AwardState? = null,
		minDateNull: Boolean,
		maxDateNull: Boolean,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
		filter: String? = null,
		pageable: Pageable,
//		finish: AwardState = AwardState.FINISH
	): Page<AwardEntity>

	@EntityGraph("awardWithDept")
	@Query(
		"""
		from AwardEntity a where 
		a.dept.id in :deptsIds and 
		((:notExclude = true) or (a.id not in :excludeAwardIds)) and 
		(
			a.type = 'S' and
			(:minDateNull = true or a.startDate >= :minDate) and 
			(:maxDateNull = true or a.endDate <= :maxDate)
		) and 
		((:filter is null) or (upper(a.name) like :filter))
		
	"""
	)
	fun findSimpleAwardByDeptIdIn(
		deptsIds: List<Long>,
		minDateNull: Boolean,
		maxDateNull: Boolean,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
		filter: String? = null,
		notExclude: Boolean = true,
		excludeAwardIds: List<Long> = emptyList(),
		pageable: Pageable
	): Page<AwardEntity>

	@Query(
		"""select new ru.md.msc.domain.award.model.AwardStateCount(
		(select count (*) from AwardEntity a where a.dept.id in :deptsIds and a.state='FINISH'),
		(select count (*) from AwardEntity a where a.dept.id in :deptsIds and a.state='NOMINEE'),
		(select count (*) from AwardEntity a where a.dept.id in :deptsIds and a.state='FUTURE'),
		(select count (*) from AwardEntity a where a.dept.id in :deptsIds and a.state='ERROR')
		)
	"""
	)
	fun countByState(
		deptsIds: List<Long>,
	): AwardStateCount

}