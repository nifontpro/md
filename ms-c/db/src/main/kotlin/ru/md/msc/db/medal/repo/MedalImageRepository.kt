package ru.md.msc.db.medal.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.medal.model.MedalImageEntity

@Repository
interface MedalImageRepository : JpaRepository<MedalImageEntity, Long> {

	@Query("from MedalImageEntity i where i.id = :imageId and i.medalId = :medalId")
	fun findByIdAndMedalId(imageId: Long, medalId: Long): MedalImageEntity?

}