package ru.md.msgal.db.folder.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.md.msgal.db.folder.repo.FolderRepository
import ru.md.msgal.domain.folder.service.FolderService

@Service
@Transactional
class FolderServiceImpl(
	private val folderRepository: FolderRepository
) : FolderService {

}