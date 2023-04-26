package ru.md.msc.db.award.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.md.msc.db.award.model.mapper.toAwardDetails
import ru.md.msc.db.award.model.mapper.toAwardDetailsEntity
import ru.md.msc.db.award.repo.AwardDetailsRepository
import ru.md.msc.db.award.repo.AwardRepository
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
		println("before: ${awardDetails.award.startDate}")
		awardDetailsRepository.save(awardDetailsEntity)
		println("after: ${awardDetails.award.startDate}")
		return awardDetailsEntity.toAwardDetails()
	}

}