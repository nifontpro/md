package ru.md.base_db.dirty.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.md.base_db.dirty.model.mappers.toDirtyLink
import ru.md.base_db.dirty.model.mappers.toDirtyLinkEntity
import ru.md.base_db.dirty.repo.DirtyLinkRepo
import ru.md.base_domain.dirty.model.DirtyLink
import ru.md.base_domain.dirty.service.DirtyLinkService

@Service
class DirtyLinkServiceImpl(
	private val dirtyLinkRepo: DirtyLinkRepo
) : DirtyLinkService {

	@Transactional
	override fun add(dirtyLink: DirtyLink): DirtyLink {
		val dirtyLinkEntity = dirtyLink.toDirtyLinkEntity()
		dirtyLinkRepo.save(dirtyLinkEntity)
		return dirtyLinkEntity.toDirtyLink()
	}

}