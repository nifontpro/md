package ru.md.msc.s3.repository

import com.amazonaws.services.s3.AmazonS3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Repository
import ru.md.base_domain.image.model.FileData
import ru.md.base_domain.image.model.IBaseImage
import ru.md.base_domain.image.model.ImageType
import ru.md.msc.domain.image.repository.S3Repository
import java.io.File

@Repository
class S3RepositoryImpl(
	private val s3: AmazonS3
) : S3Repository {

	private fun getBucketName(system: Boolean) =
		if (system) Constants.S3_BUCKET_SYSTEM else Constants.S3_BUCKET_NAME

	override suspend fun putObject(key: String, fileData: FileData): String? {
		return try {
			val bucket = getBucketName(fileData.system)
			withContext(Dispatchers.IO) {
				val file = File(fileData.url)
				s3.putObject(bucket, key, file)
				s3.getUrl(bucket, key).toExternalForm()
			}
		} catch (e: Exception) {
			println(e.message)
			null
		}
	}

	override suspend fun deleteObject(key: String, system: Boolean) {
		withContext(Dispatchers.IO) {
			s3.deleteObject(getBucketName(system), key)
		}
	}

	override suspend fun deleteBaseImage(entity: IBaseImage) {
		if (entity.type == ImageType.USER) {
			deleteObject(entity.imageKey)
		}
	}

	/**
	 * Проверка доступности хранилища
	 */
	override suspend fun available(): Boolean {
		return try {
			withContext(Dispatchers.IO) {
				s3.listBuckets().map {
					println(it.name)
					it.name
				}.contains(Constants.S3_BUCKET_NAME)
			}
		} catch (e: Exception) {
			false
		}
	}
}