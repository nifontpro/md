package ru.md.msgal.domain.folder.biz.proc

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail

fun FolderContext.folderNotFoundError() {
	fail(
		errorDb(
			repository = "folder",
			violationCode = "not found",
			description = "Папка не найдена"
		)
	)
}

fun FolderContext.getFolderError() {
	fail(
		errorDb(
			repository = "folder",
			violationCode = "get error",
			description = "Ошибка получения папки"
		)
	)
}

class FolderNotFoundException(message: String = "") : RuntimeException(message)
