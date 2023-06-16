package ru.md.base_rest

import org.imgscalr.Scalr
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.image.model.FileData
import ru.md.base_rest.Constants.LOCAL_FOLDER
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.util.*
import javax.imageio.ImageIO

fun saveFile(multipartFile: MultipartFile, entityId: Long = 0): FileData? {
	val fileBytes = multipartFile.bytes
	val fileSize = multipartFile.size
	if (fileBytes.isEmpty()) return null

	val fileExtension = multipartFile.originalFilename?.takeLastWhile { it != '.' }

//	val fileName = originalFileName.toString().split('.')[0] + "#" +
//					UUID.randomUUID().toString() + "." + fileExtension

	val fileName = UUID.randomUUID().toString() + "." + fileExtension

	return try {
		val folder = File(LOCAL_FOLDER)
		folder.mkdirs()
		val url = "$LOCAL_FOLDER/$fileName"
		val miniUrl = "$LOCAL_FOLDER/mini-$fileName"

		// сжимаем изображение
		// https://simplesolution.dev/java-resize-image-file-using-imgscalr/
		val originalImage = byteArrayToBufferedImage(fileBytes)
		val resizedImage = Scalr.resize(originalImage, Constants.COMPRESS_IMAGE_SIZE)
		val resizedFile = File(miniUrl)
		ImageIO.write(resizedImage, fileExtension, resizedFile)
		originalImage.flush()
		resizedImage.flush()

		val file = File(url)
		file.writeBytes(fileBytes)
		FileData(
			entityId = entityId,
			url = url,
			miniUrl = miniUrl,
			filename = fileName,
			fileExtension = fileExtension ?: "",
			size = fileSize,
		)
	} catch (e: Exception) {
		null
	}
}

fun byteArrayToBufferedImage(array: ByteArray): BufferedImage {
	val inputStream: InputStream = ByteArrayInputStream(array)
	return ImageIO.read(inputStream)
}