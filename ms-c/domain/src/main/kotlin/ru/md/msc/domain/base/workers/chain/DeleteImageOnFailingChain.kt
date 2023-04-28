package ru.md.msc.domain.base.workers.chain

import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.workers.deleteBaseImageFromS3

fun <T : BaseContext> ICorChainDsl<T>.deleteS3ImageOnFailingChain() {
	chain {
		on { state == ContextState.FAILING && baseImage.imageKey.isNotBlank() }
		deleteBaseImageFromS3("Удаляем изображения из s3")
	}
}