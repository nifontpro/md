package ru.md.msgal.domain.s3.repository

interface S3Repository {
	suspend fun putObject(key: String, fileUrl: String): String?
	suspend fun deleteObject(key: String)
}