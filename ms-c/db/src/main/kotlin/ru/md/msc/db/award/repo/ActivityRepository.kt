package ru.md.msc.db.award.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.award.model.ActivityEntity
import ru.md.msc.domain.award.model.*
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
		(:awardTypeNull = true or a.award.type = :awardType) and
		(:awardStateNull = true or a.award.state = :awardState) and
		(:minDateNull = true or a.date >= :minDate) and
		(:maxDateNull = true or a.date <= :maxDate) and 
		((:filterNull = true) or (upper(a.award.name) like :filter))
		"""
	)
	fun findActivityByUserId(
		userId: Long,
		minDateNull: Boolean,
		maxDateNull: Boolean,
		minDate: LocalDateTime,
		maxDate: LocalDateTime,
		filterNull: Boolean,
		filter: String,
		awardTypeNull: Boolean,
		awardStateNull: Boolean,
		awardType: AwardType,
		awardState: AwardState,
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
			upper(a.user.lastname) like :filter or 
			upper(a.user.firstname) like :filter
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

	/**
	 * Получение ids сотрудников, награжденных наградой
	 */
	@EntityGraph("activityWithUser")
	@Query(
		"""
		select a.user.id from ActivityEntity a where 
		a.award.id = :awardId and a.activ and 
		(:actionType is null or a.actionType = :actionType) and 
		((:filter is null) or (
			upper(a.user.lastname) like :filter or 
			upper(a.user.firstname) like :filter
		))
		"""
	)
	fun findActivityUserIdsByAwardId(
		awardId: Long,
		filter: String? = null,
		actionType: ActionType? = null,
	): List<Long>

	/**
	 * Получение ids наград, которыми уже награжден сотрудник
	 */
	@EntityGraph("activityWithAward")
	@Query(
		"""
		select a.award.id from ActivityEntity a where 
		a.user.id = :userId and a.activ and a.award.type = :awardType and
		(:actionType is null or a.actionType = :actionType) and 
		((:filter is null) or (
			upper(a.award.name) like :filter 
		))
		"""
	)
	fun findActivityAwardIdsByUserId(
		userId: Long,
		filter: String? = null,
		actionType: ActionType? = null,
		awardType: AwardType,
	): List<Long>

	@EntityGraph("activityWithUserAndAwardAndDept")
	@Query(
		"""
		from ActivityEntity a where 
		a.activ = true and a.user.dept.id in :deptIds and
		(coalesce(:minDate, null) is null or a.date >= :minDate) and
		(coalesce(:maxDate, null) is null or a.date <= :maxDate) and 
		(:awardState is null or a.award.state = :awardState) and
		((:filter is null) or (
			upper(a.user.lastname) like :filter or 
			upper(a.user.firstname) like :filter or 
			upper(a.award.name) like :filter
		))
	"""
	)
	fun findByDeptIdPage(
		deptIds: List<Long>,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
		awardState: AwardState? = null,
		filter: String? = null,
		pageable: Pageable
	): Page<ActivityEntity>

//	@EntityGraph("activityWithUserWithDept")
//	@Query(
//		"""
//		select new ru.md.msc.domain.dept.model.CountByDept(a.user.dept.id, count(*))
//			from ActivityEntity a
//			where a.user.dept.id in :deptsIds and
//				a.actionType='A' and a.activ and
//				(coalesce(:minDate, null) is null or a.date >= :minDate) and
//				(coalesce(:maxDate, null) is null or a.date <= :maxDate)
//			group by a.user.dept.id
//	"""
//	)
//	fun getActivAwardCountByDept(
//		deptsIds: List<Long>,
//		minDate: LocalDateTime? = null,
//		maxDate: LocalDateTime? = null,
//	): List<CountByDept>

	@EntityGraph("activityWithUserWithDept")
	@Query(
		"""
		select new ru.md.msc.domain.award.model.AwardCount(
				a.user.dept.id,
				a.user.dept.name,
				(select count (*) from ActivityEntity i where i.user.dept.id=a.user.dept.id and i.activ and i.actionType='A' and
					(coalesce(:minDate, null) is null or i.date >= :minDate) and (coalesce(:maxDate, null) is null or i.date <= :maxDate)
				),
				(select count (*) from ActivityEntity i where i.user.dept.id=a.user.dept.id and i.activ and i.actionType='P' and
					(coalesce(:minDate, null) is null or i.date >= :minDate) and (coalesce(:maxDate, null) is null or i.date <= :maxDate)
				)
			)
			from ActivityEntity a where a.user.dept.id in :deptsIds group by a.user.dept.id order by 2
	"""
	)
	fun getAllCountByDept(
		deptsIds: List<Long>,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
	): List<AwardCount>

	@Suppress("SqlRedundantCodeInCoalesce")
	@Query(
		"""
		select
				d.id as deptId,
				d.name as deptName,
				(select count (*) from md.activity i 
					left join users.user_data lu on i.user_id=lu.id
			  	left join dep.dept ld on lu.dept_id=ld.id
					where ld.id=d.id and i.is_activ and i.action_code='A' and
					(coalesce(:minDate, null) is null or i.date >= :minDate) and (coalesce(:maxDate, null) is null or i.date <= :maxDate)
				) as awardCount,
				(select count (*) from md.activity i 
					left join users.user_data lu on i.user_id=lu.id
			  	left join dep.dept ld on lu.dept_id=ld.id
					where ld.id=d.id and i.is_activ and i.action_code='P' and
					(coalesce(:minDate, null) is null or i.date >= :minDate) and (coalesce(:maxDate, null) is null or i.date <= :maxDate)
				) as nomineeCount 
			from md.activity as a 
				left join users.user_data u on a.user_id=u.id
			  left join dep.dept d on u.dept_id=d.id
			where d.id in :deptsIds group by d.id
	""",
		countQuery = """
			select count(*) from md.activity as a
				left join users.user_data u on a.user_id=u.id
			  left join dep.dept d on u.dept_id=d.id
			where d.id in :deptsIds group by d.id
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
	 * Количество сотрудников имеющих и не имеющих награды WW - with/without
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

	fun countByUserIdAndActiv(
		userId: Long,
		activ: Boolean = true
	): Long

}
