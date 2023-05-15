package ru.md.msc.rest.base

import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.image.model.FileData
import java.io.File
import java.util.*

fun saveFile(multipartFile: MultipartFile, entityId: Long): FileData? {
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
		val file = File(url)
		file.writeBytes(fileBytes)
		FileData(
			entityId = entityId,
			url = url,
			filename = fileName,
			size = fileSize,
		)
	} catch (e: Exception) {
		null
	}
}