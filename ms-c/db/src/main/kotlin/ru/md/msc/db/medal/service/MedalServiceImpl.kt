package ru.md.msc.db.medal.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.msc.db.dept.service.DeptUtil
import ru.md.msc.db.medal.model.mapper.toMedal
import ru.md.msc.db.medal.model.mapper.toMedalDetails
import ru.md.msc.db.medal.model.mapper.toMedalDetailsEntity
import ru.md.msc.db.medal.repo.ActRepository
import ru.md.msc.db.medal.repo.MedalDetailsRepository
import ru.md.msc.db.medal.repo.MedalImageRepository
import ru.md.msc.db.medal.repo.MedalRepository
import ru.md.msc.domain.medal.biz.proc.MedalNotFoundException
import ru.md.msc.domain.medal.model.Medal
import ru.md.msc.domain.medal.model.MedalDetails
import ru.md.msc.domain.medal.service.MedalService

@Service
class MedalServiceImpl(
	private val medalRepository: MedalRepository,
	private val medalDetailsRepository: MedalDetailsRepository,
	private val medalImageRepository: MedalImageRepository,
	private val actRepository: ActRepository,
	private val deptUtil: DeptUtil,
) : MedalService {

	@Transactional
	override fun create(medalDetails: MedalDetails): MedalDetails {
		val medalDetailsEntity = medalDetails.toMedalDetailsEntity()
		medalDetailsRepository.save(medalDetailsEntity)
		return medalDetailsEntity.toMedalDetails()
	}

	@Transactional
	override fun update(medalDetails: MedalDetails): MedalDetails {
		val oldMedalDetailsEntity = medalDetailsRepository.findByIdOrNull(medalDetails.medal.id) ?: run {
			throw MedalNotFoundException()
		}

		with(oldMedalDetailsEntity) {
			medalEntity.name = medalDetails.medal.name
			medalEntity.score = medalDetails.medal.score
			description = medalDetails.description
		}

		return oldMedalDetailsEntity.toMedalDetails()
	}

	@Transactional
	override fun deleteById(medalId: Long) {
		medalRepository.deleteById(medalId)
	}

	override fun findMedalById(medalId: Long): Medal {
		val medalEntity = medalRepository.findByIdOrNull(medalId) ?: throw MedalNotFoundException()
		return medalEntity.toMedal()
	}

	override fun findMedalDetailsById(medalId: Long): MedalDetails {
		val medalDetailsEntity = medalDetailsRepository.findByIdOrNull(medalId) ?: throw MedalNotFoundException()
		return medalDetailsEntity.toMedalDetails()
	}

}