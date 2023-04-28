package ru.md.msc.db.award.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.award.model.image.AwardImageEntity

@Repository
interface AwardImageRepository : JpaRepository<AwardImageEntity, Long> {

	@Query("from AwardImageEntity i where i.id = :imageId and i.awardId = :awardId")
	fun findByIdAndAwardId(imageId: Long, awardId: Long): AwardImageEntity?

}