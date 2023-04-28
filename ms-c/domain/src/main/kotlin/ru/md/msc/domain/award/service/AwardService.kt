package ru.md.msc.domain.award.service

import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.domain.image.model.BaseImage

interface AwardService {
	fun create(awardDetails: AwardDetails): AwardDetails
	fun update(awardDetails: AwardDetails): AwardDetails
	fun findByIdLazy(awardId: Long): Award
	fun findById(awardId: Long): AwardDetails
	fun findDeptIdByAwardId(awardId: Long): Long
	fun deleteById(awardId: Long)
	suspend fun addImage(awardId: Long, baseImage: BaseImage): BaseImage
	suspend fun deleteImage(awardId: Long, imageId: Long): BaseImage
}