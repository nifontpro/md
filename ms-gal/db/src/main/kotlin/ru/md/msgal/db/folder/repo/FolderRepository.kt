package ru.md.msgal.db.folder.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msgal.db.folder.model.FolderEntity

@Repository
interface FolderRepository : JpaRepository<FolderEntity, Long>