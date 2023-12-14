package ru.md.base_domain.s3.repo

import ru.md.base_domain.image.model.IBaseImage
import java.io.InputStream

interface BaseS3Repository {
	suspend fun available(): Boolean
	suspend fun putObject(key: String, fileUrl: String): String?
	suspend fun deleteBaseImage(entity: IBaseImage)
	suspend fun deleteImages(images: List<IBaseImage>)
	suspend fun putObjectMem(key: String, imageName: String, contentType: String, data: Pair<InputStream, Int>): String
}