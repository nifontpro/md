package ru.md.msc.domain.image.repository

import ru.md.base_domain.image.model.FileData
import ru.md.base_domain.image.model.IBaseImage

interface S3Repository {
	suspend fun available(): Boolean
	suspend fun putObject(key: String, fileData: FileData): String?
	suspend fun deleteObject(key: String, system: Boolean = false)
	suspend fun deleteBaseImage(entity: IBaseImage)
}