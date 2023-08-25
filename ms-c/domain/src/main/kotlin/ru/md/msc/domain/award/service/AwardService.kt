package ru.md.msc.domain.award.service

import ru.md.base_domain.gallery.SmallItem
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.msc.domain.award.model.*

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
	fun findActivAwardsByUser(userId: Long, awardType: AwardType?, baseQuery: BaseQuery): PageResult<Activity>
	fun findActivAwardsByDept(deptId: Long, awardState: AwardState?, baseQuery: BaseQuery): PageResult<Activity>
	fun findUsersByActivAward(awardId: Long, actionType: ActionType?, baseQuery: BaseQuery): PageResult<Activity>
	fun addGalleryImage(awardId: Long, smallItem: SmallItem): BaseImage
	fun findActiveCountByDepts(deptId: Long, baseQuery: BaseQuery): List<AwardCount>
	fun findCountBySubdepts(deptId: Long, baseQuery: BaseQuery): AwardStateCount
	fun findActiveCountByDeptsNative(deptId: Long, baseQuery: BaseQuery): PageResult<AwardCount>
	fun getWWAwardCount(deptId: Long, baseQuery: BaseQuery): WWAwardCount
	fun setMainImage(awardId: Long): BaseImage?
	fun updateAllAwardImg()
	fun findSimpleAwardUserAvailable(deptId: Long, userId: Long, baseQuery: BaseQuery): PageResult<Award>
	fun findBySubDept(deptId: Long, awardState: AwardState?, baseQuery: BaseQuery): PageResult<Award>
	fun findBySubDeptUserExclude(
		deptId: Long,
		userId: Long,
		actionType: ActionType,
		baseQuery: BaseQuery
	): PageResult<Award>
}