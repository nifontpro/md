package ru.md.base_s3.repo

import com.amazonaws.services.s3.AmazonS3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import ru.md.base_domain.errors.S3DeleteException
import ru.md.base_domain.image.model.IBaseImage
import ru.md.base_domain.image.model.ImageType
import ru.md.base_domain.s3.repo.BaseS3Repository
import java.io.File

@Repository
class BaseS3RepositoryImpl(
	private val s3: AmazonS3,
	@Value("\${s3.bucket.name}") private val bucketName: String,
) : BaseS3Repository {

	val log: Logger = LoggerFactory.getLogger(BaseS3RepositoryImpl::class.java)

	override suspend fun putObject(key: String, fileUrl: String): String? {
		return try {
			withContext(Dispatchers.IO) {
				val file = File(fileUrl)
				s3.putObject(bucketName, key, file)
				s3.getUrl(bucketName, key).toExternalForm()
			}
		} catch (e: Exception) {
			log.error(e.message)
			null
		}
	}

	fun deleteObject(key: String) {
		try {
			s3.deleteObject(bucketName, key)
		} catch (e: Exception) {
			throw S3DeleteException()
		}
	}

	fun deleteImage(image: IBaseImage) {
		if (image.type == ImageType.USER) {
			image.originKey?.let { deleteObject(it) }
			log.info("Object ${image.originKey} deleted")
			if (image.normalKey != image.originKey) {
				image.normalKey?.let {
					deleteObject(it)
					log.info("Object $it deleted")
				}
			}
			if (image.miniKey != image.originKey) {
				image.miniKey?.let {
					deleteObject(it)
					log.info("Object $it deleted")
				}
			}
		}
	}

	override suspend fun deleteBaseImage(entity: IBaseImage) {
		withContext(Dispatchers.IO) {
			deleteImage(entity)
		}
	}

	override suspend fun deleteImages(images: List<IBaseImage>) {
		withContext(Dispatchers.IO) {
			images.forEach {
				launch {
					deleteImage(it)
				}
			}
		}
	}

	/**
	 * Проверка доступности хранилища
	 */
	override suspend fun available(): Boolean {
		return try {
			withContext(Dispatchers.IO) {
				s3.listBuckets().map {
					it.name
				}.contains(bucketName)
			}
		} catch (e: Exception) {
			false
		}
	}
}