package ru.md.msc.domain.award.service

import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.domain.image.model.BaseImage
import ru.md.msc.domain.image.model.FileData

interface AwardService {
	fun create(awardDetails: AwardDetails): AwardDetails
	fun update(awardDetails: AwardDetails): AwardDetails
	fun findByIdLazy(awardId: Long): Award
	fun findById(awardId: Long): AwardDetails
	fun findDeptIdByAwardId(awardId: Long): Long
	fun deleteById(awardId: Long)
	suspend fun addImageToS3(awardId: Long, fileData: FileData): BaseImage
	suspend fun addImageToDb(awardId: Long, baseImage: BaseImage): BaseImage
}