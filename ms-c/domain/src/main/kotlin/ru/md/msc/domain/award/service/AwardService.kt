package ru.md.msc.domain.award.service

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.gallery.SmallItem
import ru.md.base_domain.model.BaseOrder
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.msc.domain.award.model.Activity
import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.domain.award.model.AwardState

interface AwardService {
	fun create(awardDetails: AwardDetails): AwardDetails
	fun update(awardDetails: AwardDetails): AwardDetails
	fun findByIdLazy(awardId: Long): Award
	fun findById(awardId: Long): AwardDetails
	fun findDeptIdByAwardId(awardId: Long): Long
	fun deleteById(awardId: Long)
	fun addImage(awardId: Long, baseImage: BaseImage): BaseImage
	fun deleteImage(awardId: Long, imageId: Long): BaseImage
	fun sendActivity(activity: Activity): Activity
	fun findActivAwardsByUser(userId: Long, orders: List<BaseOrder> = emptyList()): List<Activity>
	fun findActivAwardsByDept(deptId: Long, baseQuery: BaseQuery): PageResult<Activity>
	fun findUsersByActivAward(awardId: Long, orders: List<BaseOrder>): List<Activity>
	fun findBySubDept(deptId: Long, baseQuery: BaseQuery): PageResult<Award>
	fun findByDeptId(deptId: Long, awardState: AwardState?, baseQuery: BaseQuery): PageResult<Award>
	fun addGalleryImage(awardId: Long, smallItem: SmallItem): BaseImage
	fun findCountByDept(deptId: Long): Long
	fun findCountBySubdepts(deptId: Long): Long
}