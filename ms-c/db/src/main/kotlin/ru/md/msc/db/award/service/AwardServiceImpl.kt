package ru.md.msc.db.award.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.mapper.*
import ru.md.base_domain.gallery.SmallItem
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.image.model.ImageType
import ru.md.base_domain.model.BaseOrder
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.msc.db.award.model.image.AwardImageEntity
import ru.md.msc.db.award.model.mapper.*
import ru.md.msc.db.award.repo.ActivityRepository
import ru.md.msc.db.award.repo.AwardDetailsRepository
import ru.md.msc.db.award.repo.AwardImageRepository
import ru.md.msc.db.award.repo.AwardRepository
import ru.md.msc.db.award.repo.mappers.toAwardCount
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.domain.award.biz.proc.AlreadyActionException
import ru.md.msc.domain.award.biz.proc.AwardNotFoundException
import ru.md.msc.domain.award.model.*
import ru.md.msc.domain.award.service.AwardService
import ru.md.msc.domain.base.biz.ImageNotFoundException
import ru.md.msc.domain.dept.model.AwardCount
import java.time.LocalDateTime

@Service
@Transactional
class AwardServiceImpl(
	private val awardRepository: AwardRepository,
	private val awardDetailsRepository: AwardDetailsRepository,
	private val awardImageRepository: AwardImageRepository,
	private val activityRepository: ActivityRepository,
	private val deptRepository: DeptRepository,
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
			award.type = awardDetails.award.type
			award.startDate = awardDetails.award.startDate
			award.endDate = awardDetails.award.endDate
			description = awardDetails.description
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

	override fun findByDeptId(deptId: Long, awardState: AwardState?, baseQuery: BaseQuery): PageResult<Award> {
		val awards = awardRepository.findByDeptId(
			deptId = deptId,
			name = baseQuery.filter.toSearch(),
			state = awardState,
			pageable = baseQuery.toPageRequest()
		)
		return awards.toPageResult { it.toAward() }
	}

	override fun findBySubDept(deptId: Long, baseQuery: BaseQuery): PageResult<Award> {
		val pageRequest = baseQuery.toPageRequest()
		val deptsIds = deptRepository.subTreeIds(deptId = deptId)
		val awards = awardRepository.findByDeptIdIn(
			deptsIds = deptsIds,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
			filter = baseQuery.filter?.let { "$it%" },
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
		val awardImageEntity = AwardImageEntity(
			awardId = awardId,
			imageUrl = baseImage.imageUrl,
			imageKey = baseImage.imageKey,
			type = baseImage.type,
			createdAt = LocalDateTime.now()
		)
		awardImageRepository.save(awardImageEntity)
		return awardImageEntity.toImage()
	}

	override fun addGalleryImage(awardId: Long, smallItem: SmallItem): BaseImage {
		val awardImageEntity = AwardImageEntity(
			awardId = awardId,
			imageUrl = smallItem.imageUrl,
			type = ImageType.SYSTEM,
			createdAt = LocalDateTime.now()
		)
		awardImageRepository.save(awardImageEntity)
		return awardImageEntity.toImage()
	}

	override fun deleteImage(awardId: Long, imageId: Long): BaseImage {
		val awardImageEntity = awardImageRepository.findByIdAndAwardId(awardId = awardId, imageId = imageId) ?: run {
			throw ImageNotFoundException()
		}
		awardImageRepository.delete(awardImageEntity)
		return awardImageEntity.toImage()
	}

	/**
	 * Сохраняем действие с наградой
	 */
	override fun sendActivity(activity: Activity): Activity {
		val activities = activityRepository.findByUserIdAndAwardId(
			userId = activity.user?.id ?: 0, awardId = activity.award?.id ?: 0
		)
		activities.forEach {
			if (it.activ) {
				if (it.actionType == activity.actionType) {
					throw AlreadyActionException()
				}
				it.activ = false
			}
		}
		val activityEntity = activity.toActivityEntity(create = true)
		activityRepository.save(activityEntity)
		return activityEntity.toActivity()
	}

	override fun findActivAwardsByUser(userId: Long, orders: List<BaseOrder>): List<Activity> {
		val activities = activityRepository.findByUserIdAndActiv(userId = userId, sort = orders.toSort())
		return activities.map { it.toActivityOnlyAward() }
	}

	override fun findUsersByActivAward(awardId: Long, orders: List<BaseOrder>): List<Activity> {
		val activities = activityRepository.findByAwardIdAndActiv(awardId = awardId, sort = orders.toSort())
		return activities.map { it.toActivityOnlyUser() }
	}

	override fun findActivAwardsByDept(deptId: Long, baseQuery: BaseQuery): PageResult<Activity> {
		val pageRequest = baseQuery.toPageRequest()
		val activities = activityRepository.findByDeptIdPage(
			deptId = deptId,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
			pageable = pageRequest
		)
		return activities.toPageResult { it.toActivityUserLazy() }
	}

	override fun findCountBySubdepts(deptId: Long, baseQuery: BaseQuery): AwardStateCount {
		val deptsIds = getDepts(deptId = deptId, subdepts = baseQuery.subdepts)
		return awardRepository.countByState(deptsIds = deptsIds)
	}

	/**
	 * Получить все типы награждений в подотделах
	 * subdepts=true - во всем вложенном дереве
	 * subdepts=false - в ближних наследниках
	 */
	override fun findActiveCountByDepts(deptId: Long, baseQuery: BaseQuery): List<AwardCount> {
		val deptsIds = getDepts(deptId = deptId, subdepts = baseQuery.subdepts, nearSub = true)
		return activityRepository.getAllCountByDept(
			deptsIds = deptsIds,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
		)
	}

	override fun findActiveCountByDeptsNative(deptId: Long, baseQuery: BaseQuery): PageResult<AwardCount> {
		val deptsIds = getDepts(deptId = deptId, subdepts = baseQuery.subdepts, nearSub = true)
		val count = activityRepository.getAllCountByDeptNative(
			deptsIds = deptsIds,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
			pageable = baseQuery.toPageRequest()
		)
		return count.toPageResult { it.toAwardCount() }
	}

	/**
	 * Получение списка отделов от текущей вершины дерева
	 * subdepts = true - все дерево подотделов
	 * subdepts = false:
	 *    nearSub = false (default) - только вершина
	 *            = true - непосредственные потомки
	 */
	private fun getDepts(deptId: Long, subdepts: Boolean, nearSub: Boolean = false): List<Long> {
		return if (subdepts) {
			deptRepository.subTreeIds(deptId = deptId)
		} else {
			if (nearSub) {
				deptRepository.findByParentId(parentId = deptId)
			} else {
				listOf(deptId)
			}
		}
	}

}