package ru.md.msgal.domain.item.biz.proc

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
import ru.md.msgal.domain.folder.service.FolderService
import ru.md.msgal.domain.item.biz.validate.validateItemName
import ru.md.msgal.domain.item.biz.workers.*
import ru.md.msgal.domain.item.service.ItemService
import ru.md.msgal.domain.s3.repository.S3Repository

@Component
class ItemProcessor(
	private val itemService: ItemService,
	private val folderService: FolderService,
	private val s3Repository: S3Repository,
) : IBaseProcessor<ItemContext> {

	override suspend fun exec(ctx: ItemContext) = businessChain.exec(ctx.also {
		it.itemService = itemService
		it.folderService = folderService
		it.s3Repository = s3Repository
	})

	companion object {

		private val businessChain = rootChain<ItemContext> {
			initStatus()

			operation("Создать объект галереи", ItemCommand.CREATE) {
				validateFolderId("Проверка folderId")
				validateItemName("Проверка имени объекта")
				validateFolderExist("Проверяем наличие папки")
				trimFieldItem("Очищаем поля")
				addItemImageToS3("Добавляем изображение в s3")
				addItemImageToDb("Добавляем изображение в БД")
				deleteItemImageFromS3("Удаляем изображение из s3 в случае ошибки записи в БД")
			}

			operation("Получить объекты из папки", ItemCommand.GET_BY_FOLDER) {
				validateFolderId("Проверка folderId")
				worker("Допустимые поля сортировки") { orderFields = listOf("name", "createdAt") }
				validateSortedFields("Проверка списка полей сортировки")
				getItemsByFolderId("Получаем объекты")
			}

			finishOperation()
		}.build()
	}
}