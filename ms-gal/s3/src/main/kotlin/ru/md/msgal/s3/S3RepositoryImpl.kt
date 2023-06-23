package ru.md.msgal.s3

import com.amazonaws.services.s3.AmazonS3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import ru.md.base_domain.image.model.FileData
import ru.md.msgal.domain.s3.repository.S3Repository
import java.io.File

@Repository
class S3RepositoryImpl(
	private val s3: AmazonS3
) : S3Repository {

	val log: Logger = LoggerFactory.getLogger(S3RepositoryImpl::class.java)

	override suspend fun putObject(key: String, fileData: FileData): String? {
		return try {
			withContext(Dispatchers.IO) {
				val file = File(fileData.imageUrl)
				s3.putObject(Constants.S3_BUCKET_NAME, key, file)
				s3.getUrl(Constants.S3_BUCKET_NAME, key).toExternalForm()
			}
		} catch (e: Exception) {
			log.error(e.message)
			null
		}
	}

	override suspend fun deleteObject(key: String) {
		withContext(Dispatchers.IO) {
			s3.deleteObject(Constants.S3_BUCKET_NAME, key)
		}
	}

}