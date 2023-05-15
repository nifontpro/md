package ru.md.msgal.domain.folder.service

import ru.md.msgal.domain.folder.model.Folder

interface FolderService {
	fun create(folder: Folder): Folder
}