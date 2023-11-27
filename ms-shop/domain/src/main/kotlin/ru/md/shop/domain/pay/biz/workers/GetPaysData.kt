package ru.md.shop.domain.pay.biz.workers

import ru.md.base_domain.biz.helper.pageFun
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.shop.domain.pay.biz.proc.PayContext
import ru.md.shop.domain.pay.biz.proc.getPaysDataError

fun ICorChainDsl<PayContext>.getPaysData(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		paysData = pageFun {
			payService.getPaysData(
				deptId = deptId,
				userId = userId,
				baseQuery = baseQuery,
				payCode = payCode,
				isActive = isActive
			)
		}
	}

	except {
		log.error(it.message)
		getPaysDataError()
	}
}