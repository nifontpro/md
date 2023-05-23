package ru.md.msc.db.award.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.award.model.ActivityEntity
import ru.md.msc.domain.dept.model.AllCountByDept
import ru.md.msc.domain.dept.model.CountByDept
import java.time.LocalDateTime

@Repository
interface ActivityRepository : JpaRepository<ActivityEntity, Long> {

	@Query("from ActivityEntity a where a.user.id = :userId and a.award.id = :awardId")
	fun findByUserIdAndAwardId(userId: Long, awardId: Long): List<ActivityEntity>

	//	@Query("from ActivityEntity a where a.user.id = :userId and a.activ = true")
	@EntityGraph("activityWithAward")
	fun findByUserIdAndActiv(userId: Long, activ: Boolean = true, sort: Sort): List<ActivityEntity>

	@EntityGraph("activityWithUser")
	fun findByAwardIdAndActiv(awardId: Long, activ: Boolean = true, sort: Sort): List<ActivityEntity>

	@EntityGraph("activityWithUserAndAward")
	@Query(
		"""
		from ActivityEntity a where 
		a.activ = true and a.deptId = :deptId and
		(coalesce(:minDate, null) is null or a.date >= :minDate) and
		(coalesce(:maxDate, null) is null or a.date <= :maxDate)
	"""
	)
	fun findByDeptIdPage(
		deptId: Long,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
		pageable: Pageable
	): Page<ActivityEntity>

	@Query(
		"""
		select new ru.md.msc.domain.dept.model.CountByDept(a.deptId, count(*)) 
			from ActivityEntity a
			where a.deptId in :deptsIds and  
				a.actionType='A' and a.activ and 
				(coalesce(:minDate, null) is null or a.date >= :minDate) and
				(coalesce(:maxDate, null) is null or a.date <= :maxDate)
			group by a.deptId
	"""
	)
	fun getActivAwardCountByDept(
		deptsIds: List<Long>,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
	): List<CountByDept>

	@Query(
		"""
		select new ru.md.msc.domain.dept.model.AllCountByDept(
				a.deptId,
				(select count (*) from ActivityEntity i where i.deptId=a.deptId and i.activ and i.actionType='A' and 
					(coalesce(:minDate, null) is null or i.date >= :minDate) and (coalesce(:maxDate, null) is null or i.date <= :maxDate)
				),
				(select count (*) from ActivityEntity i where i.deptId=a.deptId and i.activ and i.actionType='P' and 
					(coalesce(:minDate, null) is null or i.date >= :minDate) and (coalesce(:maxDate, null) is null or i.date <= :maxDate)
				)
			)
			from ActivityEntity a where a.deptId in :deptsIds	group by a.deptId
	"""
	)
	fun getAllCountByDept(
		deptsIds: List<Long>,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
	): List<AllCountByDept>
}
/*
	@Query(
		"""
		select new ru.md.msc.domain.dept.model.AllCountByDept(
				a.deptId,
				(select count (*) from ActivityEntity i where i.deptId=a.deptId and i.activ and i.actionType='A'),
				(select count (*) from ActivityEntity i where i.deptId=a.deptId and i.activ and i.actionType='P')
			)
			from ActivityEntity a
			where a.deptId in :deptsIds and
				(coalesce(:minDate, null) is null or a.date >= :minDate) and
				(coalesce(:maxDate, null) is null or a.date <= :maxDate)
			group by a.deptId
	"""
 */