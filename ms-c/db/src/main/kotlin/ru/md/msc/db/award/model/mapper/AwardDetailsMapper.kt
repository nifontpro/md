package ru.md.msc.db.award.model.mapper

import ru.md.base_db.image.mappers.toBaseImage
import ru.md.msc.db.award.model.AwardDetailsEntity
import ru.md.msc.domain.award.model.AwardDetails
import java.time.LocalDateTime

fun AwardDetails.toAwardDetailsEntity(create: Boolean = false) = AwardDetailsEntity(
	award = award.toAwardEntity(create = create),
	criteria = criteria,
	createdAt = if (create) LocalDateTime.now() else createdAt,
)

fun AwardDetailsEntity.toAwardDetails() = AwardDetails(
	award = award.toAward(),
	criteria = criteria,
	createdAt = createdAt,
	images = images.map { it.toBaseImage() }
)