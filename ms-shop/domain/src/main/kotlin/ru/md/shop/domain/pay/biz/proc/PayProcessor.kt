package ru.md.shop.domain.pay.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.validate.chain.validateViewSameAndAdminDeptLevelChain
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.base_domain.dept.service.BaseDeptService
import ru.md.base_domain.user.service.BaseUserService
import ru.md.cor.rootChain
import ru.md.shop.domain.base.biz.validate.chain.validateProductIdAndAccessToProductChain
import ru.md.shop.domain.base.service.BaseProductService
import ru.md.shop.domain.pay.biz.workers.getUserPayData
import ru.md.shop.domain.pay.biz.workers.payProduct
import ru.md.shop.domain.pay.service.PayService

@Component
class PayProcessor(
	private val payService: PayService,
	private val baseProductService: BaseProductService,
	private val baseDeptService: BaseDeptService,
	private val baseUserService: BaseUserService,
) : IBaseProcessor<PayContext> {

	override suspend fun exec(ctx: PayContext) = businessChain.exec(ctx.also {
		it.payService = payService
		it.baseProductService = baseProductService
		it.baseDeptService = baseDeptService
		it.baseUserService = baseUserService
	})

	companion object {

		private val businessChain = rootChain {
			initStatus()

			operation("Получит баланс счета сотрудника", PayCommand.GET_USER_PAY) {
				validateViewSameAndAdminDeptLevelChain()
				getUserPayData("Получаем баланс счета")
			}

			operation("Купить приз", PayCommand.PAY_PRODUCT) {
				validateProductIdAndAccessToProductChain()
				payProduct("Покупаем приз")
			}

			finishOperation()
		}.build()
	}
}