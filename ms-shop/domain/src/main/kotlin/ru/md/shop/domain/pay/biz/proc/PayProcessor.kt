package ru.md.shop.domain.pay.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.validate.chain.validateUserIdSameOrAdminDeptLevelChain
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.base_domain.dept.service.BaseDeptService
import ru.md.base_domain.user.biz.workers.getAuthUserAndVerifyEmail
import ru.md.base_domain.user.service.BaseUserService
import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.shop.domain.base.biz.validate.chain.validateProductIdAndAccessToProductChain
import ru.md.shop.domain.base.biz.workers.findCompanyDeptIdByOwnerOrAuthUserChain
import ru.md.shop.domain.base.service.BaseProductService
import ru.md.shop.domain.pay.biz.workers.getPaysData
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

		private val businessChain = rootChain<PayContext> {
			initStatus()

			operation("Получит баланс счета сотрудника", PayCommand.GET_USER_PAY) {
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateUserIdSameOrAdminDeptLevelChain()
				getUserPayData("Получаем баланс счета")
			}

			operation("Купить приз", PayCommand.PAY_PRODUCT) {
				validateProductIdAndAccessToProductChain()
				payProduct("Покупаем приз")
			}

			operation("Получить платежные данные", PayCommand.GET_PAYS_DATA) {
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				findUserIdOrIgnoreByAdmin()
				findCompanyDeptIdByOwnerOrAuthUserChain()
				worker("") { log.info("deptId = $deptId") }
				worker("") { log.info("userId = $userId") }
				getPaysData("Получаем платежные документы")
			}

			finishOperation()
		}.build()

		private fun ICorChainDsl<PayContext>.findUserIdOrIgnoreByAdmin() {
			/**
			 * Получение userId или возможное игнорирование его Администратором
			 * в случае вывода всех платежных данных
			 */
			chain {
				on { !userIdNotPresent }
				validateUserIdSameOrAdminDeptLevelChain()
			}
			chain {
				on { userIdNotPresent }
				chain {
					on { !isAuthUserHasAdminRole }
					worker("") {
						userId = authUser.id
						userIdNotPresent = false
					}
				}
			}
		}
	}
}