package ru.md.msc.db.award.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.msc.db.award.model.image.AwardImageEntity
import ru.md.msc.db.award.model.mapper.toAwardDetails
import ru.md.msc.db.award.model.mapper.toAwardDetailsEntity
import ru.md.msc.db.award.model.mapper.toAwardLazy
import ru.md.msc.db.award.repo.AwardDetailsRepository
import ru.md.msc.db.award.repo.AwardImageRepository
import ru.md.msc.db.award.repo.AwardRepository
import ru.md.msc.db.base.mapper.toImage
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.domain.award.biz.proc.AwardNotFoundException
import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.domain.award.service.AwardService
import ru.md.msc.domain.base.biz.ImageNotFoundException
import ru.md.msc.domain.image.model.BaseImage
import ru.md.msc.domain.image.model.FileData
import ru.md.msc.domain.image.model.ImageType
import ru.md.msc.domain.image.repository.S3Repository
import java.time.LocalDateTime

@Service
@Transactional
class AwardServiceImpl(
	private val awardRepository: AwardRepository,
	private val awardDetailsRepository: AwardDetailsRepository,
	private val awardImageRepository: AwardImageRepository,
	private val deptRepository: DeptRepository,
	private val s3Repository: S3Repository,
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

	override fun findDeptIdByAwardId(awardId: Long): Long {
		return awardRepository.finDeptId(awardId = awardId) ?: throw AwardNotFoundException()
	}

	override fun deleteById(awardId: Long) {
		awardRepository.deleteById(awardId)
	}

	override suspend fun addImageToS3(awardId: Long, fileData: FileData): BaseImage {
		val deptId = awardRepository.finDeptId(awardId = awardId) ?: throw AwardNotFoundException()
		val rootDeptId = deptRepository.getRootId(deptId = deptId) ?: throw Exception()
		val prefix = "R$rootDeptId/D$deptId/A$awardId"
		val imageKey = "$prefix/${fileData.filename}"
		val imageUrl = s3Repository.putObject(key = imageKey, fileData = fileData) ?: throw Exception()
		return BaseImage(imageUrl = imageUrl, imageKey = imageKey, type = ImageType.USER)
	}

	override suspend fun addImageToDb(awardId: Long, baseImage: BaseImage): BaseImage {
		val awardImageEntity = AwardImageEntity(
			awardId = awardId,
			imageUrl = baseImage.imageUrl,
			imageKey = baseImage.imageKey,
			type = ImageType.USER,
			createdAt = LocalDateTime.now()
		)
		awardImageRepository.save(awardImageEntity)
		return awardImageEntity.toImage()
	}

	override suspend fun deleteImage(awardId: Long, imageId: Long): BaseImage {
		val userImageEntity = awardImageRepository.findByIdAndAwardId(awardId = awardId, imageId = imageId) ?: run {
			throw ImageNotFoundException()
		}
		awardImageRepository.delete(userImageEntity)
		return userImageEntity.toImage()
	}

}