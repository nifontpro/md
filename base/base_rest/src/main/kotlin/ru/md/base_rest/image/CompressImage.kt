package ru.md.base_rest.image

import org.imgscalr.Scalr
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.image.model.ImageData
import ru.md.base_rest.Constants.MINI_COMPRESS_IMAGE_SIZE
import ru.md.base_rest.Constants.NORM_COMPRESS_IMAGE_SIZE
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import javax.imageio.ImageIO

fun compressImage(
	multipartFile: MultipartFile,
	entityId: Long = 0,
	imageType: ImageType
): ImageData {
	val fileBytes = multipartFile.bytes
	if (fileBytes.isEmpty()) throw ConvertImageException("Пустой файл с изображением")
	val imageName = UUID.randomUUID().toString() + "." + imageType.format

	var normCompress = true
	var miniIs: Pair<InputStream, Int>? = null
	var normIs: Pair<InputStream, Int>? = null
	if (imageType.compress) {
		// сжимаем изображение
		// https://simplesolution.dev/java-resize-image-file-using-imgscalr/
		val originalImage = fileBytes.toBufferedImage()
		val resizedMiniImage = try {
			Scalr.resize(originalImage, MINI_COMPRESS_IMAGE_SIZE)
		} catch (e: Exception) {
			originalImage.flush()
			throw ConvertImageException("Ошибка конвертации изображения до размера $MINI_COMPRESS_IMAGE_SIZE x $MINI_COMPRESS_IMAGE_SIZE")
		}
		miniIs = resizedMiniImage.toInputStream(imageType.format)
		resizedMiniImage.flush()

		if (originalImage.width > NORM_COMPRESS_IMAGE_SIZE) {
			val resizedNormImage = try {
				Scalr.resize(originalImage, NORM_COMPRESS_IMAGE_SIZE)
			} catch (e: Exception) {
				originalImage.flush()
				throw ConvertImageException("Ошибка конвертации изображения до размера $NORM_COMPRESS_IMAGE_SIZE x $NORM_COMPRESS_IMAGE_SIZE")
			}
			normIs = resizedNormImage.toInputStream(imageType.format)
			resizedNormImage.flush()

		} else {
			normCompress = false
		}
		originalImage.flush()
	}

	return ImageData(
		entityId = entityId,
		normalImage = normIs,
		miniImage = miniIs,
		originImage = ByteArrayInputStream(fileBytes) to fileBytes.size,
		imageName = imageName,
		contentType = imageType.contentType,
		compress = imageType.compress,
		normCompress = normCompress
	)
}

private fun ByteArray.toBufferedImage(): BufferedImage {
	return ImageIO.read(ByteArrayInputStream(this))
}

private fun BufferedImage.toInputStream(format: String): Pair<InputStream, Int> {
	val outputStream = ByteArrayOutputStream()
	ImageIO.write(this, format, outputStream)
	val bytes = outputStream.toByteArray()
	val size = bytes.size
	val inputStream = ByteArrayInputStream(bytes)
	outputStream.close()
	return inputStream to size
}

class ConvertImageException(message: String = "") : RuntimeException(message)