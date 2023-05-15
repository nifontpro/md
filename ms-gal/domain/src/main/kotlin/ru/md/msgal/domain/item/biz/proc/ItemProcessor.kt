package ru.md.msgal.domain.item.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.cor.rootChain
import ru.md.msgal.domain.base.workers.finishOperation
import ru.md.msgal.domain.base.workers.initStatus
import ru.md.msgal.domain.base.workers.operation
import ru.md.msgal.domain.item.service.ItemService

@Component
class ItemProcessor(
	private val itemService: ItemService,
) : IBaseProcessor<ItemContext> {

	override suspend fun exec(ctx: ItemContext) = businessChain.exec(ctx.also {
		it.itemService = itemService
	})

	companion object {

		private val businessChain = rootChain<ItemContext> {
			initStatus()

			operation("Создать файл", ItemCommand.CREATE) {

			}

			finishOperation()
		}.build()
	}
}