package ru.md.msc.db.award.repo

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.award.model.ActivityEntity

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
	fun findByDeptIdAndActiv(deptId: Long, activ: Boolean = true, sort: Sort): List<ActivityEntity>

}