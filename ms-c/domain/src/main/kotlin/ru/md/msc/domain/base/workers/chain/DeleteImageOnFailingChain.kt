package ru.md.msc.domain.base.workers.chain

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.msc.domain.base.workers.image.deleteBaseImageFromS3

fun <T : BaseClientContext> ICorChainDsl<T>.deleteS3ImageOnFailingChain() {
	chain {
		on { state == ContextState.FAILING && baseImage.imageKey.isNotBlank() }
		deleteBaseImageFromS3("Удаляем изображения из s3")
	}
}