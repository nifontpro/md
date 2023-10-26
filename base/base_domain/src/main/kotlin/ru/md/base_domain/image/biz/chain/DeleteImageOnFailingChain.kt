package ru.md.base_domain.image.biz.chain

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.base_domain.image.biz.workers.deleteBaseImageFromS3

fun <T : BaseMedalsContext> ICorChainDsl<T>.deleteS3ImageOnFailingChain() {
	chain {
		on { state == ContextState.FAILING }
		deleteBaseImageFromS3("Удаляем изображения из s3")
	}
}