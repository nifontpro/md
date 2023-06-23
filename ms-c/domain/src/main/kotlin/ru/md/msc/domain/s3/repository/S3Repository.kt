package ru.md.msc.domain.s3.repository

import ru.md.base_domain.image.model.IBaseImage

interface S3Repository {
	suspend fun available(): Boolean
	suspend fun putObject(key: String, fileUrl: String): String?
	suspend fun deleteObject(key: String)
	suspend fun deleteBaseImage(entity: IBaseImage)
}