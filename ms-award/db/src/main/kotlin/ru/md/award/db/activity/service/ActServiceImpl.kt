package ru.md.award.db.activity.service

import org.springframework.stereotype.Service
import ru.md.award.db.activity.repo.ActRepository
import ru.md.award.domain.activity.service.ActService

@Service
class ActServiceImpl(
	private val actRepository: ActRepository
): ActService {



}