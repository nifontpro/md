package ru.md.base_domain.biz.validate.chain

import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.cor.ICorChainDsl
import ru.md.base_domain.biz.validate.validatePageNumber
import ru.md.base_domain.biz.validate.validatePageSize

fun <T: BaseMedalsContext> ICorChainDsl<T>.validatePageParamsChain() {
	validatePageNumber("Проверка номера страницы")
	validatePageSize("Проверка размера страниц")
}