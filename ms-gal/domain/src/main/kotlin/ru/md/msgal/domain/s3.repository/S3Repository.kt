package ru.md.msgal.domain.s3.repository

import ru.md.base_domain.image.model.FileData

interface S3Repository {
	suspend fun putObject(key: String, fileData: FileData): String?
	suspend fun deleteObject(key: String)
}