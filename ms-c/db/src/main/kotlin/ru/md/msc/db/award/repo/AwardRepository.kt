package ru.md.msc.db.award.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.award.model.AwardEntity

@Repository
interface AwardRepository : JpaRepository<AwardEntity, Long> {

	@Query("select a.dept.id from AwardEntity a where a.id = :awardId")
	fun finDeptId(awardId: Long): Long?
}