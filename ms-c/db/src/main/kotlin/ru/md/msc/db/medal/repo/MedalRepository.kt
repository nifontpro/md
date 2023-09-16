package ru.md.msc.db.medal.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.medal.model.MedalEntity

@Repository
interface MedalRepository : JpaRepository<MedalEntity, Long> {

	@Modifying
	@Query("delete from MedalEntity a where a.id = :medalId")
	override fun deleteById(medalId: Long)

}