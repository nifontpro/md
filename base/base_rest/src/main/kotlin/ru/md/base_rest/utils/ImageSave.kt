package ru.md.base_rest.utils

import org.imgscalr.Scalr
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.image.model.FileData
import ru.md.base_rest.Constants.LOCAL_FOLDER
import ru.md.base_rest.Constants.MINI_COMPRESS_IMAGE_SIZE
import ru.md.base_rest.Constants.NORM_COMPRESS_IMAGE_SIZE
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.util.*
import javax.imageio.ImageIO

fun saveImageFile(multipartFile: MultipartFile, entityId: Long = 0, compress: Boolean = true): FileData {
	val fileBytes = multipartFile.bytes
	val fileSize = multipartFile.size
	if (fileBytes.isEmpty()) throw ImageSaveException("Пустой файл")

	val fileExtension = multipartFile.originalFilename?.takeLastWhile { it != '.' }

//	val fileName = originalFileName.toString().split('.')[0] + "#" +
//					UUID.randomUUID().toString() + "." + fileExtension

	val fileName = UUID.randomUUID().toString() + "." + fileExtension

	val folder = File(LOCAL_FOLDER)
	folder.mkdirs()
	val originUrl = "$LOCAL_FOLDER/origin-$fileName"
	val miniUrl = if (compress) "$LOCAL_FOLDER/mini-$fileName" else originUrl
	var normUrl = if (compress) "$LOCAL_FOLDER/norm-$fileName" else originUrl
	var normCompress = true
	if (compress) {
		// сжимаем изображение
		// https://simplesolution.dev/java-resize-image-file-using-imgscalr/
		val originalImage = byteArrayToBufferedImage(fileBytes)
		val resizedMiniImage = try {
			Scalr.resize(originalImage, MINI_COMPRESS_IMAGE_SIZE)
		} catch (e: Exception) {
			originalImage.flush()
			throw ImageSaveException("Ошибка конвертации изображения до размера $MINI_COMPRESS_IMAGE_SIZE x $MINI_COMPRESS_IMAGE_SIZE")
		}
		val resizedMiniFile = File(miniUrl)
		try {
			ImageIO.write(resizedMiniImage, fileExtension, resizedMiniFile)
		} catch (e: Exception) {
			originalImage.flush()
			throw ImageSaveException("Ошибка записи изображения $MINI_COMPRESS_IMAGE_SIZE x $MINI_COMPRESS_IMAGE_SIZE ")
		} finally {
			resizedMiniImage.flush()
		}

		if (originalImage.width > NORM_COMPRESS_IMAGE_SIZE) {
			val resizedNormImage = try {
				Scalr.resize(originalImage, NORM_COMPRESS_IMAGE_SIZE)
			} catch (e: Exception) {
				originalImage.flush()
				throw ImageSaveException("Ошибка конвертации изображения до размера $NORM_COMPRESS_IMAGE_SIZE x $NORM_COMPRESS_IMAGE_SIZE")
			}
			val resizedNormFile = File(normUrl)
			try {
				ImageIO.write(resizedNormImage, fileExtension, resizedNormFile)
			} catch (e: Exception) {
				originalImage.flush()
				throw ImageSaveException("Ошибка записи изображения $NORM_COMPRESS_IMAGE_SIZE")
			} finally {
				resizedNormImage.flush()
			}
		} else {
			normUrl = originUrl
			normCompress = false
		}

		originalImage.flush()
	}

	val file = File(originUrl)
	try {
		file.writeBytes(fileBytes)
	} catch (e: Exception) {
		throw ImageSaveException("Ошибка записи оригинального изображения")
	}
	return FileData(
		entityId = entityId,
		normalUrl = normUrl,
		miniUrl = miniUrl,
		originUrl = originUrl,
		filename = fileName,
		fileExtension = fileExtension ?: "",
		size = fileSize,
		compress = compress,
		normCompress = normCompress
	)
}

fun byteArrayToBufferedImage(array: ByteArray): BufferedImage {
	val inputStream: InputStream = ByteArrayInputStream(array)
	return ImageIO.read(inputStream)
}

class ImageSaveException(message: String = "") : RuntimeException(message)