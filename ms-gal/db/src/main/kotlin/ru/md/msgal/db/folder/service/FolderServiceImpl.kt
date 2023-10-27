package ru.md.msgal.db.folder.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.base.mapper.toSort
import ru.md.base_domain.model.BaseOrder
import ru.md.msgal.db.folder.model.mapper.toFolder
import ru.md.msgal.db.folder.model.mapper.toFolderEntity
import ru.md.msgal.db.folder.repo.FolderRepository
import ru.md.msgal.domain.folder.biz.proc.FolderNotFoundException
import ru.md.msgal.domain.folder.model.Folder
import ru.md.msgal.domain.folder.service.FolderService
import java.time.LocalDateTime

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

	override fun update(folder: Folder): Folder {
		val oldFolderEntity = folderRepository.findByIdOrNull(id = folder.id) ?: throw FolderNotFoundException()
		with(oldFolderEntity) {
			name = folder.name
			description = folder.description
			updatedAt = LocalDateTime.now()
		}
		return oldFolderEntity.toFolder()
	}

	override fun delete(folderId: Long): Folder {
		val folderEntity = folderRepository.findByIdOrNull(id = folderId) ?: throw FolderNotFoundException()
		folderRepository.delete(folderEntity)
		return folderEntity.toFolder()
	}

	override fun getAll(orders: List<BaseOrder>): List<Folder> {
		val folders = folderRepository.findAll(sort = orders.toSort())
		return folders.map { it.toFolder() }
	}

	override fun doesFolderExist(folderId: Long): Boolean {
		return folderRepository.countById(folderId) > 0
	}

}