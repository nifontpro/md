package ru.md.msgal.domain.folder.biz.proc

import org.springframework.stereotype.Component
import ru.md.cor.rootChain
import ru.md.msgal.domain.base.biz.IBaseProcessor
import ru.md.msgal.domain.base.workers.finishOperation
import ru.md.msgal.domain.base.workers.initStatus
import ru.md.msgal.domain.base.workers.operation
import ru.md.msgal.domain.folder.biz.workers.createFolder
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
				createFolder("Создаем папку")
			}

			finishOperation()
		}.build()
	}
}