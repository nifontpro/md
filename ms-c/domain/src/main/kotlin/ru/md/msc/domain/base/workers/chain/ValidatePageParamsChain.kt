package ru.md.msc.domain.base.workers.chain

import ru.md.cor.ICorChainDsl
import ru.md.msc.domain.base.validate.validateRequiredPageable
import ru.md.msc.domain.base.validate.validatePageNumber
import ru.md.msc.domain.base.validate.validatePageSize
import ru.md.msc.domain.user.biz.proc.UserContext

fun ICorChainDsl<UserContext>.validatePageParamsChain() {
	validatePageNumber("Проверка номера страницы")
	validatePageSize("Проверка размера страниц")
}

fun ICorChainDsl<UserContext>.validateRequiredPageParamsChain() {
	validateRequiredPageable("Проверка обязательных параметров пагинации")
	validatePageParamsChain()
}