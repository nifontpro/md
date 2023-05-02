package ru.md.msc.domain.award.service

import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.domain.base.model.BaseOrder
import ru.md.msc.domain.image.model.BaseImage

interface AwardService {
	fun create(awardDetails: AwardDetails): AwardDetails
	fun update(awardDetails: AwardDetails): AwardDetails
	fun findByIdLazy(awardId: Long): Award
	fun findById(awardId: Long): AwardDetails
	fun findDeptIdByAwardId(awardId: Long): Long
	fun deleteById(awardId: Long)
	fun addImage(awardId: Long, baseImage: BaseImage): BaseImage
	fun deleteImage(awardId: Long, imageId: Long): BaseImage
	fun findByDeptId(deptId: Long, orders: List<BaseOrder> = emptyList()): List<Award>
}