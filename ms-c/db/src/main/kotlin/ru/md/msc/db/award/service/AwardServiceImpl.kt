package ru.md.msc.db.award.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.base.mapper.toPageRequest
import ru.md.base_db.base.mapper.toPageResult
import ru.md.base_db.base.mapper.toSearchUpperOrNull
import ru.md.base_db.dept.repo.BaseDeptRepository
import ru.md.base_db.image.mappers.toBaseImage
import ru.md.base_domain.errors.ImageNotFoundException
import ru.md.base_domain.gallery.SmallItem
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.image.model.ImageType
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.base_domain.pay.service.BaseUserPayService
import ru.md.base_domain.user.biz.errors.UserNotFoundException
import ru.md.msc.db.award.model.image.AwardImageEntity
import ru.md.msc.db.award.model.mapper.*
import ru.md.msc.db.award.repo.ActivityRepository
import ru.md.msc.db.award.repo.AwardDetailsRepository
import ru.md.msc.db.award.repo.AwardImageRepository
import ru.md.msc.db.award.repo.AwardRepository
import ru.md.msc.db.award.repo.mappers.toAwardCount
import ru.md.msc.db.dept.service.DeptUtil
import ru.md.msc.domain.award.biz.proc.AlreadyActionException
import ru.md.msc.domain.award.biz.proc.AwardNotFoundException
import ru.md.msc.domain.award.model.*
import ru.md.msc.domain.award.service.AwardService
import java.time.LocalDateTime

@Service
@Transactional
class AwardServiceImpl(
	private val awardRepository: AwardRepository,
	private val awardDetailsRepository: AwardDetailsRepository,
	private val awardImageRepository: AwardImageRepository,
	private val activityRepository: ActivityRepository,
	private val baseDeptRepository: BaseDeptRepository,
	private val deptUtil: DeptUtil,
	private val baseUserPayService: BaseUserPayService,
) : AwardService {

	override fun create(awardDetails: AwardDetails): AwardDetails {
		val awardDetailsEntity = awardDetails.toAwardDetailsEntity(create = true)
		awardDetailsRepository.save(awardDetailsEntity)
		return awardDetailsEntity.toAwardDetails()
	}

	override fun update(awardDetails: AwardDetails): AwardDetails {
		val oldAwardDetailsEntity = awardDetailsRepository.findByIdOrNull(awardDetails.award.id) ?: run {
			throw AwardNotFoundException()
		}
		with(oldAwardDetailsEntity) {
			award.name = awardDetails.award.name
			award.description = awardDetails.award.description
			award.type = awardDetails.award.type
			award.startDate = awardDetails.award.startDate
			award.endDate = awardDetails.award.endDate
			criteria = awardDetails.criteria
		}
		return oldAwardDetailsEntity.toAwardDetails()
	}

	override fun findByIdLazy(awardId: Long): Award {
		val awardEntity = awardRepository.findByIdOrNull(awardId) ?: throw AwardNotFoundException()
		return awardEntity.toAwardLazy()
	}

	override fun findById(awardId: Long): AwardDetails {
		val awardDetailsEntity = awardDetailsRepository.findByIdOrNull(awardId) ?: throw AwardNotFoundException()
		return awardDetailsEntity.toAwardDetails()
	}

	override fun findBySubDept(
		deptId: Long,
		awardState: AwardState?,
		withUsers: Boolean,
		baseQuery: BaseQuery
	): PageResult<Award> {
		val pageRequest = baseQuery.toPageRequest()
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)

		return if (withUsers) {
			awardRepository.findByDeptIdInWithUsers(
				deptsIds = deptsIds,
				state = awardState,
				minDate = baseQuery.minDate,
				maxDate = baseQuery.maxDate,
				filter = baseQuery.filter.toSearchUpperOrNull(),
				pageable = pageRequest
			).toPageResult { it.toAwardWithDeptAndUsers() }
		} else {
			awardRepository.findByDeptIdIn(
				deptsIds = deptsIds,
				state = awardState,
				minDate = baseQuery.minDate,
				maxDate = baseQuery.maxDate,
				filter = baseQuery.filter.toSearchUpperOrNull(),
				pageable = pageRequest
			).toPageResult { it.toAward() }
		}
	}

	override fun findBySubDeptUserExclude(
		deptId: Long,
		userId: Long,
		actionType: ActionType,
		baseQuery: BaseQuery
	): PageResult<Award> {

		val filter = baseQuery.filter.toSearchUpperOrNull()

		val excludeAwardIds = activityRepository.findActivityAwardIdsByUserId(
			userId = userId,
			filter = filter,
			actionType = actionType.takeIf { it != ActionType.UNDEF },
			awardType = AwardType.PERIOD
		)

		val pageRequest = baseQuery.toPageRequest()
		val deptsIds = baseDeptRepository.subTreeIds(deptId = deptId)

		val awards = awardRepository.findByDeptIdIn(
			deptsIds = deptsIds,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
			filter = filter,
			notExclude = excludeAwardIds.isEmpty(),
			excludeAwardIds = excludeAwardIds,
			pageable = pageRequest
		)
		return awards.toPageResult { it.toAward() }
	}

	override fun findSimpleAwardUserAvailable(
		deptId: Long,
		userId: Long,
		baseQuery: BaseQuery
	): PageResult<Award> {

		val filter = baseQuery.filter.toSearchUpperOrNull()

		val excludeAwardIds = activityRepository.findActivityAwardIdsByUserId(
			userId = userId,
			filter = filter,
			actionType = ActionType.AWARD,
			awardType = AwardType.SIMPLE
		)

		val pageRequest = baseQuery.toPageRequest()
		val deptsIds = baseDeptRepository.subTreeIds(deptId = deptId)

		val awards = awardRepository.findSimpleAwardByDeptIdIn(
			deptsIds = deptsIds,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
			filter = filter,
			notExclude = excludeAwardIds.isEmpty(),
			excludeAwardIds = excludeAwardIds,
			pageable = pageRequest
		)
		return awards.toPageResult { it.toAward() }
	}

	override fun findDeptIdByAwardId(awardId: Long): Long {
		return awardRepository.findDeptId(awardId = awardId) ?: throw AwardNotFoundException()
	}

	override fun deleteById(awardId: Long) {
		awardRepository.deleteById(awardId)
	}

	override fun addImage(awardId: Long, baseImage: BaseImage): BaseImage {
		awardImageRepository.deleteAllAwardImage(awardId)
		val awardImageEntity = AwardImageEntity(
			awardId = awardId,
			originUrl = baseImage.originUrl,
			originKey = baseImage.originKey,
			normalUrl = baseImage.normalUrl,
			normalKey = baseImage.normalKey,
			miniUrl = baseImage.miniUrl,
			miniKey = baseImage.miniKey,
			type = baseImage.type,
			createdAt = LocalDateTime.now()
		)
		awardImageRepository.save(awardImageEntity)
		return awardImageEntity.toBaseImage()
	}

	override fun addGalleryImage(awardId: Long, smallItem: SmallItem): BaseImage {
		val awardImageEntity = AwardImageEntity(
			awardId = awardId,
			normalUrl = smallItem.normalUrl,
			miniUrl = smallItem.miniUrl,
			type = ImageType.SYSTEM,
			createdAt = LocalDateTime.now()
		)
		awardImageRepository.save(awardImageEntity)
		return awardImageEntity.toBaseImage()
	}

	override fun setMainImage(awardId: Long): BaseImage? {
		val awardDetailsEntity = awardDetailsRepository.findByIdOrNull(awardId) ?: throw AwardNotFoundException()
		val awardEntity = awardDetailsEntity.award

		var awardImageEntity = awardDetailsEntity.images.firstOrNull() ?: run {
			awardEntity.mainImg = null
			awardEntity.normImg = null
			return null
		}

		awardDetailsEntity.images.forEach {
			if (it.createdAt > awardImageEntity.createdAt) {
				awardImageEntity = it
			} else if (it.main) {
				it.main = false
			}
		}

		awardImageEntity.main = true
		awardEntity.mainImg = if (awardImageEntity.miniUrl != null) awardImageEntity.miniUrl else awardImageEntity.normalUrl
		awardEntity.normImg = awardImageEntity.normalUrl
		return awardImageEntity.toBaseImage()
	}

	override fun deleteImage(awardId: Long, imageId: Long): BaseImage {
		val awardImageEntity = awardImageRepository.findByIdAndAwardId(awardId = awardId, imageId = imageId) ?: run {
			throw ImageNotFoundException()
		}
		awardImageRepository.delete(awardImageEntity)
		return awardImageEntity.toBaseImage()
	}

	/**
	 * Сохраняем действие с наградой
	 */
	override fun sendActivity(activity: Activity): Activity {
		val award = activity.award ?: throw AwardNotFoundException()
		val activities = activityRepository.findByUserIdAndAwardId(
			userId = activity.user?.id ?: 0, awardId = activity.award?.id ?: 0
		)

		activities.forEach {
			if (it.activ) {
				if (it.actionType == activity.actionType && award.type == AwardType.PERIOD) {
					throw AlreadyActionException()
				}
				it.activ = false
			}
		}
		val activityEntity = activity.toActivityEntity(create = true)
		activityRepository.save(activityEntity)

		if (activity.actionType == ActionType.AWARD) {
			val userId = activity.user?.id ?: throw UserNotFoundException()
			baseUserPayService.changeBalance(
				userId = userId,
				delta = award.score,
				comment = "Пополнение баланса за награду ${award.name}"
			)
		}

		return activityEntity.toActivity()
	}

	override fun findActivAwardsByUser(
		userId: Long,
		awardType: AwardType?,
		awardState: AwardState?,
		baseQuery: BaseQuery
	): PageResult<Activity> {
		val activities = activityRepository.findActivityByUserId(
			userId = userId,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
			filter = baseQuery.filter.toSearchUpperOrNull(),
			awardType = awardType,
			awardState = awardState,
			pageable = baseQuery.toPageRequest()
		)
		return activities.toPageResult { it.toActivityOnlyAward() }
	}

	override fun findUsersByActivAward(
		awardId: Long,
		actionType: ActionType?,
		baseQuery: BaseQuery
	): PageResult<Activity> {
		val activities = activityRepository.findActivityByAwardId(
			awardId = awardId,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
			filter = baseQuery.filter.toSearchUpperOrNull(),
			actionType = actionType,
			pageable = baseQuery.toPageRequest()
		)
		return activities.toPageResult { it.toActivityOnlyUser() }
	}

	override fun findActivAwardsByDept(
		deptId: Long,
		awardState: AwardState?,
		baseQuery: BaseQuery
	): PageResult<Activity> {
		val pageRequest = baseQuery.toPageRequest()
		val deptIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)
		val activities = activityRepository.findByDeptIdPage(
			deptIds = deptIds,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
			awardState = awardState,
			filter = baseQuery.filter.toSearchUpperOrNull(),
			pageable = pageRequest
		)
		return activities.toPageResult { it.toActivity() }
	}

	override fun findCountBySubdepts(deptId: Long, baseQuery: BaseQuery): AwardStateCount {
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)
		return awardRepository.countByState(deptsIds = deptsIds)
	}

	/**
	 * Получить все типы награждений в подотделах
	 * subdepts=true - во всем вложенном дереве
	 * subdepts=false - в ближних наследниках
	 */
	override fun findActiveCountByDepts(deptId: Long, baseQuery: BaseQuery): List<AwardCount> {
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts, nearSub = true)
		return activityRepository.getAllCountByDept(
			deptsIds = deptsIds,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
		)
	}

	override fun findActiveCountByDeptsNative(deptId: Long, baseQuery: BaseQuery): PageResult<AwardCount> {
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts, nearSub = true)
		val count = activityRepository.getGroupAwardCountByDept(
			deptsIds = deptsIds,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
			pageable = baseQuery.toPageRequest()
		)
		return count.toPageResult { it.toAwardCount() }
	}

	override fun getWWAwardCount(deptId: Long, baseQuery: BaseQuery): WWAwardCount {
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)
		return activityRepository.getWWAwardCount(
			deptsIds = deptsIds,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
		)
	}

	override fun updateAllAwardImg() {
		val awards = awardRepository.findAll()
		awards.forEach {
			val id = it.id ?: return@forEach
			val img = setMainImage(id)
			println(img)
		}
	}

}