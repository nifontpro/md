package ru.md.msc.domain.image.repository

import ru.md.msc.domain.image.model.FileData

interface S3Repository {
	suspend fun putObject(key: String, fileData: FileData): String?
	suspend fun deleteObject(key: String, system: Boolean = false): Boolean
	suspend fun available(): Boolean
//	suspend fun deleteAllImages(entity: IImages): Boolean
}