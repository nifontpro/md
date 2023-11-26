package ru.md.shop.domain.pay.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.validate.chain.validatePageParamsChain
import ru.md.base_domain.biz.validate.chain.validateUserIdSameOrAdminDeptLevelChain
import ru.md.base_domain.biz.validate.validateAdminRole
import ru.md.base_domain.biz.validate.validateAuthDeptLevel
import ru.md.base_domain.biz.validate.validateSortedFields
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.base_domain.dept.biz.validate.validateDeptId
import ru.md.base_domain.dept.service.BaseDeptService
import ru.md.base_domain.user.biz.workers.getAuthUserAndVerifyEmail
import ru.md.base_domain.user.service.BaseUserService
import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.shop.domain.base.biz.validate.chain.validateProductIdAndAccessToProductChain
import ru.md.base_domain.dept.biz.workers.chain.findCompanyDeptIdByOwnerOrAuthUserChain
import ru.md.shop.domain.base.service.BaseProductService
import ru.md.shop.domain.pay.biz.validate.*
import ru.md.shop.domain.pay.biz.workers.*
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
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateUserIdSameOrAdminDeptLevelChain()
				getUserPayData("Получаем баланс счета")
			}

			operation("Купить приз", PayCommand.PAY_PRODUCT) {
				validateProductIdAndAccessToProductChain()
				payProduct("Покупаем приз")
			}

			operation("Получить платежные данные", PayCommand.GET_PAYS_DATA) {
				validatePageParamsChain()
				setGetPaysDataSortedFields("Устанавливаем сортировочные поля")
				validateSortedFields("Проверяем сортировочные поля")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				findUserIdOrIgnoreByAdmin()
				findCompanyDeptIdByOwnerOrAuthUserChain()
				getPaysData("Получаем платежные документы")
			}

			operation("Выдача приза Админом", PayCommand.ADMIN_GIVE_PRODUCT) {
				validatePayDataId("Проверяем payDataId")
				getPayDataById("Получаем платежку")
				validateActivePayData("Проверка выбранной операции на доступность")
				validatePayDataPayCodePAY("Проверяем состояние операции - PAY")
				validateAdminAccessToPayData()
				givePayProductByAdmin("Админ выдает приз")
			}

			operation("Возврат приза Админом", PayCommand.ADMIN_RETURN_PRODUCT) {
				validatePayDataId("Проверяем payDataId")
				getPayDataById("Получаем платежку")
				validateActivePayData("Проверка выбранной операции на доступность")
				validatePayDataPayCodeGIVEN("Проверяем состояние операции - GIVEN")
				validateAdminAccessToPayData()
				returnProduct("Возвращаем приз")
			}

			operation("Возврат приза Админом", PayCommand.USER_RETURN_PRODUCT) {
				validatePayDataId("Проверяем payDataId")
				getPayDataById("Получаем платежку")
				validateActivePayData("Проверка выбранной операции на доступность")
				validatePayDataPayCodePAY("Проверяем состояние операции - PAY")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validatePayDataEqAthUser("Проверка, чтоб операция принадлежала пользователю")
				returnProduct("Возвращаем приз")
			}

			finishOperation()
		}.build()

		private fun ICorChainDsl<PayContext>.validateAdminAccessToPayData() {
			getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
			worker("") { deptId = payData.product.deptId }
			validateDeptId("Проверка deptId")
			validateAdminRole("Проверка наличия прав Администратора")
			validateAuthDeptLevel("Проверка доступа к отделу")
		}

		private fun ICorChainDsl<PayContext>.findUserIdOrIgnoreByAdmin() {
			/**
			 * Получение userId или возможное игнорирование его Администратором
			 * в случае вывода всех платежных данных
			 * доступ по deptId должен быть проверен другим запросом
			 */
			chain {
				on { !isAuthUserHasAdminRole }
				worker("") { userId = authUser.id }
			}
		}
	}
}