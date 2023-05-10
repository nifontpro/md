package ru.md.msc.db.award.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.award.model.ActivityEntity
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

	/*	@EntityGraph("activityWithUserAndAward")
		fun findByDeptIdAndActiv(
			deptId: Long,
			activ: Boolean = true,
			sort: Sort
		): List<ActivityEntity>*/

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

}