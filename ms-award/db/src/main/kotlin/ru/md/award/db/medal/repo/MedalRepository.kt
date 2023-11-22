package ru.md.award.db.medal.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.award.db.medal.model.MedalEntity

@Repository
interface MedalRepository : JpaRepository<MedalEntity, Long> {

	@Modifying
	@Query("delete from MedalEntity a where a.id = :medalId")
	override fun deleteById(medalId: Long)

	@Query("select m.deptEntity.id from MedalEntity m where m.id = :medalId")
	fun findDeptId(medalId: Long): Long?

}