package ru.md.msgal.domain.folder.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.validate.validateSortedFields
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.msgal.domain.base.validate.validateFolderExist
import ru.md.msgal.domain.base.validate.validateFolderId
import ru.md.msgal.domain.folder.biz.validate.validateFolderName
import ru.md.msgal.domain.folder.biz.validate.validateParentFolderId
import ru.md.msgal.domain.folder.biz.workers.*
import ru.md.msgal.domain.folder.service.FolderService

@Component
class FolderProcessor(
	private val folderService: FolderService,
) : IBaseProcessor<FolderContext> {

	override suspend fun exec(ctx: FolderContext) = businessChain.exec(ctx.also {
		it.folderService = folderService
	})

	companion object {

		private val businessChain = rootChain<FolderContext> {
			initStatus()

			operation("Создать папку", FolderCommand.CREATE) {
				validateFolderName("Проверка имени папки")
				validateParentFolderId("Проверяем parentId")
				worker("Подготовка") { folderId = folder.parentId }
				validateFolderExist("Проверяем наличие папки")
				trimFieldFolder("Очищаем поля папки")
				createFolder("Создаем папку")
			}

			operation("Обновить папку", FolderCommand.UPDATE) {
				validateFolderId("Проверяем folderId")
				validateFolderName("Проверка имени папки")
				trimFieldFolder("Очищаем поля папки")
				updateFolder("Обновляем папку")
			}

			operation("Удалить папку", FolderCommand.DELETE) {
				validateFolderId("Проверяем folderId")
				deleteFolder("Удаляем папку")
			}

			operation("Получить все папки", FolderCommand.GET_ALL) {
				worker("Допустимые поля сортировки") { orderFields = listOf("parentId", "name", "createdAt") }
				validateSortedFields("Проверка списка полей сортировки")
				getAllFolders("Получаем все папки")
			}

			finishOperation()
		}.build()
	}
}