package ru.md.msgal.db.folder.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.md.msgal.db.folder.model.mapper.toFolder
import ru.md.msgal.db.folder.model.mapper.toFolderEntity
import ru.md.msgal.db.folder.repo.FolderRepository
import ru.md.msgal.domain.folder.model.Folder
import ru.md.msgal.domain.folder.service.FolderService

@Service
@Transactional
class FolderServiceImpl(
	private val folderRepository: FolderRepository
) : FolderService {

	override fun create(folder: Folder): Folder {
		val folderEntity = folder.toFolderEntity(create = true)
		folderRepository.save(folderEntity)
		return folderEntity.toFolder()
	}

}