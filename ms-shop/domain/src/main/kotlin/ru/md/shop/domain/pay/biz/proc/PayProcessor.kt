package ru.md.shop.domain.pay.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.validate.chain.validateViewSameAndAdminDeptLevelChain
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.base_domain.dept.service.BaseDeptService
import ru.md.base_domain.user.biz.validate.validateUserId
import ru.md.base_domain.user.service.BaseUserService
import ru.md.cor.rootChain
import ru.md.shop.domain.pay.biz.workers.getUserPayData
import ru.md.shop.domain.pay.service.UserPayService

@Component
class PayProcessor(
	private val userPayService: UserPayService,
	private val baseDeptService: BaseDeptService,
	private val baseUserService: BaseUserService,
) : IBaseProcessor<PayContext> {

	override suspend fun exec(ctx: PayContext) = businessChain.exec(ctx.also {
		it.userPayService = userPayService
		it.baseDeptService = baseDeptService
		it.baseUserService = baseUserService
	})

	companion object {

		private val businessChain = rootChain<PayContext> {
			initStatus()

			operation("Получит баланс счета сотрудника", PayCommand.GET_USER_PAY) {
				validateUserId("Проверка userId")
				validateViewSameAndAdminDeptLevelChain()
				getUserPayData("Получаем баланс счета")
			}

			finishOperation()
		}.build()
	}
}