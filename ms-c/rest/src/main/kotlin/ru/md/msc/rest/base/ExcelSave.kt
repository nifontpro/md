package ru.md.msc.rest.base

import org.springframework.web.multipart.MultipartFile
import ru.md.base_rest.Constants.LOCAL_FOLDER
import java.io.File
import java.util.*

fun saveExcelFile(multipartFile: MultipartFile): String {
	val fileBytes = multipartFile.bytes
	if (fileBytes.isEmpty()) throw FileSaveException("Пустой файл")

	val fileExtension = multipartFile.originalFilename?.takeLastWhile { it != '.' }
	val fileName = UUID.randomUUID().toString() + "." + fileExtension

	val folder = File(LOCAL_FOLDER)
	folder.mkdirs()
	val url = "$LOCAL_FOLDER/$fileName"

	val file = File(url)
	try {
		file.writeBytes(fileBytes)
	} catch (e: Exception) {
		throw FileSaveException("Ошибка записи файла")
	}
	return url
}

class FileSaveException(message: String = "") : RuntimeException(message)