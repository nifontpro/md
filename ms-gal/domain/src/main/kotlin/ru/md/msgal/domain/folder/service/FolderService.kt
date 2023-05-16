package ru.md.msgal.domain.folder.service

import ru.md.base_domain.model.BaseOrder
import ru.md.msgal.domain.folder.model.Folder

interface FolderService {
	fun create(folder: Folder): Folder
	fun update(folder: Folder): Folder
	fun delete(folderId: Long): Folder
	fun getAll(orders: List<BaseOrder>): List<Folder>
}