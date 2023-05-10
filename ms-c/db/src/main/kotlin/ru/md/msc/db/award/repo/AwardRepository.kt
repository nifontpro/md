package ru.md.msc.db.award.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.award.model.AwardEntity
import java.time.LocalDateTime

@Repository
interface AwardRepository : JpaRepository<AwardEntity, Long> {

	@Query("select a.dept.id from AwardEntity a where a.id = :awardId")
	fun finDeptId(awardId: Long): Long?

	@Modifying
	@Query("delete from AwardEntity a where a.id = :awardId")
	override fun deleteById(awardId: Long)

	@EntityGraph("awardWithDept")
	fun findByDeptId(deptId: Long, sort: Sort): List<AwardEntity>

	@EntityGraph("awardWithDept")
	@Query(
		"""
		from AwardEntity a where 
		a.dept.id in :deptsIds and 
		(
			a.type = 'P' and 
			a.startDate <= NOW() and (coalesce(:minDate, null) is null or a.startDate >= :minDate) and
			a.endDate >= NOW() and (coalesce(:maxDate, null) is null or a.endDate <= :maxDate)
		) or (
			a.type = 'S' and
			(coalesce(:minDate, null) is null or a.startDate >= :minDate) and 
			(coalesce(:maxDate, null) is null or a.endDate <= :maxDate)
		)
	"""
	)
	fun findByDeptIdIn(
		deptsIds: List<Long>,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
		pageable: Pageable
	): Page<AwardEntity>
}