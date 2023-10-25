package ru.md.base_domain.s3.repo

import ru.md.base_domain.image.model.IBaseImage

interface BaseS3Repository {
	suspend fun available(): Boolean
	suspend fun putObject(key: String, fileUrl: String): String?
	suspend fun deleteObject(key: String)
	suspend fun deleteBaseImage(entity: IBaseImage)
}