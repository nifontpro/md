package ru.md.msc.db.user.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.md.msc.db.user.model.gender.FemaleEntity
import ru.md.msc.db.user.model.gender.MaleEntity
import ru.md.msc.db.user.model.gender.MaleLastnameEntity
import ru.md.msc.db.user.repo.FemaleRepository
import ru.md.msc.db.user.repo.MaleLastnameRepository
import ru.md.msc.db.user.repo.MaleRepository
import ru.md.msc.domain.user.service.GenderService

@Service
class GenderServiceImpl(
	private val maleRepository: MaleRepository,
	private val femaleRepository: FemaleRepository,
	private val maleLastnameRepository: MaleLastnameRepository,
) : GenderService {

	@Transactional
	override fun addMaleName(name: String) {
		maleRepository.save(MaleEntity(name))
	}

	@Transactional
	override fun addFemaleName(name: String) {
		femaleRepository.save(FemaleEntity(name))
	}

	@Transactional
	override fun addMaleLastname(name: String) {
		maleLastnameRepository.save(MaleLastnameEntity(name))
	}

}