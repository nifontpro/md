package ru.md.msc.db.award.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.md.msc.db.award.model.ActivityEntity
import ru.md.msc.domain.dept.model.AwardCount
import ru.md.msc.domain.dept.model.CountByDept
import ru.md.msc.domain.dept.model.IAwardCount
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
		a.activ = true and a.dept.id = :deptId and
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
		select new ru.md.msc.domain.dept.model.CountByDept(a.dept.id, count(*)) 
			from ActivityEntity a
			where a.dept.id in :deptsIds and  
				a.actionType='A' and a.activ and 
				(coalesce(:minDate, null) is null or a.date >= :minDate) and
				(coalesce(:maxDate, null) is null or a.date <= :maxDate)
			group by a.dept.id
	"""
	)
	fun getActivAwardCountByDept(
		deptsIds: List<Long>,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
	): List<CountByDept>

	@Query(
		"""
		select new ru.md.msc.domain.dept.model.AwardCount(
				a.dept.id,
				(select d.name from DeptEntity d where d.id = a.dept.id) ,
				(select count (*) from ActivityEntity i where i.dept.id=a.dept.id and i.activ and i.actionType='A' and
					(coalesce(:minDate, null) is null or i.date >= :minDate) and (coalesce(:maxDate, null) is null or i.date <= :maxDate)
				),
				(select count (*) from ActivityEntity i where i.dept.id=a.dept.id and i.activ and i.actionType='P' and
					(coalesce(:minDate, null) is null or i.date >= :minDate) and (coalesce(:maxDate, null) is null or i.date <= :maxDate)
				)
			)
			from ActivityEntity a where a.dept.id in :deptsIds group by a.dept.id order by 2
	"""
	)
	@EntityGraph("activityWithDept")
	fun getAllCountByDept(
		deptsIds: List<Long>,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
	): List<AwardCount>

	@Query(
		"""
		select
				a.dept_id as deptId,
				(select d.name from dep.dept d where d.id = a.dept_id) as deptName,
				(select count (*) from md.activity i where i.dept_id=a.dept_id and i.is_activ and i.action_code='A' and
					(coalesce(:minDate, null) is null or i.date >= :minDate) and (coalesce(:maxDate, null) is null or i.date <= :maxDate)
				) as awardCount,
				(select count (*) from md.activity i where i.dept_id=a.dept_id and i.is_activ and i.action_code='P' and
					(coalesce(:minDate, null) is null or i.date >= :minDate) and (coalesce(:maxDate, null) is null or i.date <= :maxDate)
				) as nomineeCount
			from md.activity as a where a.dept_id in :deptsIds group by a.dept_id
	""",
		countQuery = """
			select count(*) from md.activity as a where a.dept_id in :deptsIds group by a.dept_id
		""",
		nativeQuery = true,
	)
	fun getAllCountByDeptNative(
		@Param("deptsIds") deptsIds: List<Long>,
		@Param("minDate") minDate: LocalDateTime? = null,
		@Param("maxDate") maxDate: LocalDateTime? = null,
		pageable: Pageable
	): Page<IAwardCount>

}

/*
		"""
		select
				a.dept_id as deptId,
				(select d.name from dep.dept d where d.id = a.dept_id) as deptName,
				(select count (*) from md.activity i where i.dept_id=a.dept_id and i.is_activ and i.action_code='A' and
					(coalesce(?2, null) is null or i.date >= ?2) and (coalesce(?3, null) is null or i.date <= ?3)
				) as awardCount,
				(select count (*) from md.activity i where i.dept_id=a.dept_id and i.is_activ and i.action_code='P' and
					(coalesce(?2, null) is null or i.date >= ?2) and (coalesce(?3, null) is null or i.date <= ?3)
				) as nomineeCount
			from md.activity as a where a.dept_id in ?1 group by a.dept_id
	""",
 */

