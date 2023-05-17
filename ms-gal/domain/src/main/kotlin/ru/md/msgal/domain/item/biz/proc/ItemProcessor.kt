package ru.md.msgal.domain.item.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.cor.rootChain
import ru.md.msgal.domain.base.validate.validateFolderId
import ru.md.msgal.domain.item.biz.validate.validateItemName
import ru.md.msgal.domain.item.biz.workers.addItemImageToS3
import ru.md.msgal.domain.item.biz.workers.trimFieldItem
import ru.md.msgal.domain.item.service.ItemService
import ru.md.msgal.domain.s3.repository.S3Repository

@Component
class ItemProcessor(
	private val itemService: ItemService,
	private val s3Repository: S3Repository,
) : IBaseProcessor<ItemContext> {

	override suspend fun exec(ctx: ItemContext) = businessChain.exec(ctx.also {
		it.itemService = itemService
		it.s3Repository = s3Repository
	})

	companion object {

		private val businessChain = rootChain {
			initStatus()

			operation("Создать элемент галереи", ItemCommand.CREATE) {
				validateFolderId("Проверка folderId")
				validateItemName("Проверка имени объекта")
				trimFieldItem("Очищаем поля")
				addItemImageToS3("Добавляем изображение в s3")
			}

			finishOperation()
		}.build()
	}
}