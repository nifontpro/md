package ru.md.msc.db.award.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.msc.db.award.model.mapper.toAwardDetails
import ru.md.msc.db.award.model.mapper.toAwardDetailsEntity
import ru.md.msc.db.award.model.mapper.toAwardLazy
import ru.md.msc.db.award.repo.AwardDetailsRepository
import ru.md.msc.db.award.repo.AwardRepository
import ru.md.msc.domain.award.biz.proc.AwardNotFoundException
import ru.md.msc.domain.award.model.Award
import ru.md.msc.domain.award.model.AwardDetails
import ru.md.msc.domain.award.service.AwardService

@Service
@Transactional
class AwardServiceImpl(
	private val awardRepository: AwardRepository,
	private val awardDetailsRepository: AwardDetailsRepository,
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

}