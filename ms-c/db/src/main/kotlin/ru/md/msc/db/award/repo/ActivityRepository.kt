package ru.md.msc.db.award.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.award.model.ActivityEntity
import ru.md.msc.domain.award.model.*
import ru.md.msc.domain.dept.model.CountByDept
import java.time.LocalDateTime

@Repository
interface ActivityRepository : JpaRepository<ActivityEntity, Long> {

	@Query("from ActivityEntity a where a.user.id = :userId and a.award.id = :awardId")
	fun findByUserIdAndAwardId(userId: Long, awardId: Long): List<ActivityEntity>

	@EntityGraph("activityWithAward")
	@Query(
		"""
		from ActivityEntity a where 
		a.user.id = :userId and a.activ and 
		(:awardState is null or a.award.state = :awardState) and
		(coalesce(:minDate, null) is null or a.date >= :minDate) and
		(coalesce(:maxDate, null) is null or a.date <= :maxDate) and 
		((:filter is null) or
			(upper(a.award.name) like upper(:filter))
		)
		"""
	)
	fun findActivityByUserId(
		userId: Long,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
		filter: String? = null,
		awardState: AwardState? = null,
		pageable: Pageable
	): Page<ActivityEntity>

	@EntityGraph("activityWithUser")
	@Query(
		"""
		from ActivityEntity a where 
		a.award.id = :awardId and a.activ and 
		(:actionType is null or a.actionType = :actionType) and 
		(coalesce(:minDate, null) is null or a.date >= :minDate) and
		(coalesce(:maxDate, null) is null or a.date <= :maxDate) and 
		((:filter is null) or (
			upper(a.user.lastname) like upper(:filter) or 
			upper(a.user.firstname) like upper(:filter)
		))
		"""
	)
	fun findActivityByAwardId(
		awardId: Long,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
		filter: String? = null,
		actionType: ActionType? = null,
		pageable: Pageable
	): Page<ActivityEntity>

	//	@EntityGraph("activityWithUserAndAwardAndDept")
	@EntityGraph("activityWithUserAndAward")
	@Query(
		"""
		from ActivityEntity a where 
		a.activ = true and a.dept.id = :deptId and
		(coalesce(:minDate, null) is null or a.date >= :minDate) and
		(coalesce(:maxDate, null) is null or a.date <= :maxDate) and 
		(:awardState is null or a.award.state = :awardState) and
		((:filter is null) or (
			upper(a.user.lastname) like upper(:filter) or 
			upper(a.user.firstname) like upper(:filter) or 
			upper(a.award.name) like upper(:filter)
		))
	"""
	)
	fun findByDeptIdPage(
		deptId: Long,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
		awardState: AwardState? = null,
		filter: String? = null,
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
		select new ru.md.msc.domain.award.model.AwardCount(
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

	@Suppress("SqlRedundantCodeInCoalesce")
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
			select count(*) from md.activity as a where a.dept_id in (:deptsIds) group by a.dept_id
		""",
		nativeQuery = true,
	)
	fun getGroupAwardCountByDept(
		deptsIds: List<Long>,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
		pageable: Pageable
	): Page<IAwardCount>

	/**
	 * Количество наград и номинаций по отделам за период
	 */
	/*	@Query(
			"""
			select new ru.md.msc.domain.award.model.AwardCount(
				0,
				'depts',
				(select count (*) from ActivityEntity a where a.dept.id in :deptsIds and a.activ and a.actionType='A' and
					(coalesce(:minDate, null) is null or a.date >= :minDate) and (coalesce(:maxDate, null) is null or a.date <= :maxDate)
				),
				(select count (*) from ActivityEntity a where a.dept.id in :deptsIds and a.activ and a.actionType='P' and
					(coalesce(:minDate, null) is null or a.date >= :minDate) and (coalesce(:maxDate, null) is null or a.date <= :maxDate)
				)
			)
		"""
		)
		fun getSumAwardCountByDepts(
			deptsIds: List<Long>,
			minDate: LocalDateTime? = null,
			maxDate: LocalDateTime? = null,
		): AwardCount*/

	/**
	 * Количество сотрудников имеющих и не имеющих награды
	 */
	@Query(
		"""
		select new ru.md.msc.domain.award.model.WWAwardCount(
			(select count(u.id) from UserEntity u where u.dept.id in (:deptsIds) and 
				(select count(*) from ActivityEntity a where a.activ and a.actionType='A' and a.user.id=u.id and 
					(coalesce(:minDate, null) is null or a.date >= :minDate) and (coalesce(:maxDate, null) is null or a.date <= :maxDate)
				)>1
			),
			(select count(u.id) from UserEntity u where u.dept.id in (:deptsIds) and 
				(select count(*) from ActivityEntity a where a.activ and a.actionType='A' and a.user.id=u.id and 
					(coalesce(:minDate, null) is null or a.date >= :minDate) and (coalesce(:maxDate, null) is null or a.date <= :maxDate)
				)<1
			)
		)
	"""
	)
	fun getWWAwardCount(
		deptsIds: List<Long>,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
	): WWAwardCount

}
