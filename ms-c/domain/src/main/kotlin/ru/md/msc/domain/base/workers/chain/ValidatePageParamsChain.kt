package ru.md.msc.domain.base.workers.chain

import ru.md.cor.ICorChainDsl
import ru.md.msc.domain.base.biz.BaseClientContext
import ru.md.base_domain.biz.validate.validatePageNumber
import ru.md.base_domain.biz.validate.validatePageSize

fun <T: BaseClientContext> ICorChainDsl<T>.validatePageParamsChain() {
	validatePageNumber("Проверка номера страницы")
	validatePageSize("Проверка размера страниц")
}