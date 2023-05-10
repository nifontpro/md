package ru.md.msc.domain.base.workers.chain

import ru.md.cor.ICorChainDsl
import ru.md.msc.domain.base.biz.BaseContext
import ru.md.msc.domain.base.validate.validatePageNumber
import ru.md.msc.domain.base.validate.validatePageSize

fun <T: BaseContext> ICorChainDsl<T>.validatePageParamsChain() {
	validatePageNumber("Проверка номера страницы")
	validatePageSize("Проверка размера страниц")
}