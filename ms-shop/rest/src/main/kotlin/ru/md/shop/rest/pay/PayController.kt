@file:Suppress("KDocUnresolvedReference")

package ru.md.shop.rest.pay

import org.springframework.web.bind.annotation.*
import ru.md.base_domain.model.BaseResponse
import ru.md.base_rest.base.authProcess
import ru.md.base_rest.model.request.AUTH
import ru.md.base_rest.utils.JwtUtils
import ru.md.shop.domain.pay.biz.proc.PayProcessor
import ru.md.shop.rest.pay.mappers.fromTransport
import ru.md.shop.rest.pay.mappers.toTransportPayDataResponse
import ru.md.shop.rest.pay.mappers.toTransportPaysDataResponse
import ru.md.shop.rest.pay.mappers.toTransportUserPayResponse
import ru.md.shop.rest.pay.model.request.*
import ru.md.shop.rest.pay.model.response.PayDataResponse
import ru.md.shop.rest.pay.model.response.UserPayResponse

@RestController
@RequestMapping("pay")
class PayController(
	private val payProcessor: PayProcessor,
	private val jwtUtils: JwtUtils,
) {

	/**
	 * Получение данных счета Сотрудника
	 * [userId] - необходимо указать Администратору, счет какого Сотрудника нужно получить,
	 *  если не указан, выводится собственный счет.
	 */
	@PostMapping("get_user")
	private suspend fun getUserPay(
		@RequestBody request: GetUserPayRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<UserPayResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = payProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserPayResponse() }
		)
	}

	/**
	 * Покупка приза
	 */
	@PostMapping("pay_product")
	private suspend fun payProduct(
		@RequestBody request: PayProductRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<PayDataResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = payProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportPayDataResponse() }
		)
	}

	/**
	 * Выдать приз Сотруднику со склада Администратором.
	 * [payDataId] - номер платежной операции при покупке приза
	 */
	@PostMapping("give_admin")
	private suspend fun giveProductFromAdmin(
		@RequestBody request: GiveProductRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<PayDataResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = payProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportPayDataResponse() }
		)
	}

	/**
	 * Возврат уже выданного приза Сотрудником с возвратом ему средств на счет.
	 * Операцию выполняет Администратор.
	 * [payDataId] - номер платежной операции при выдаче приза
	 */
	@PostMapping("return_admin")
	private suspend fun returnProductAdmin(
		@RequestBody request: AdminReturnProductRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<PayDataResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = payProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportPayDataResponse() }
		)
	}

	/**
	 * Возврат еще не выданного приза Сотрудником с возвратом ему средств на счет.
	 * Операцию выполняет сам Сотрудник.
	 * [payDataId] - номер платежной операции при покупке приза
	 */
	@PostMapping("return_user")
	private suspend fun returnProductUser(
		@RequestBody request: UserReturnProductRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<PayDataResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = payProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportPayDataResponse() }
		)
	}

	/**
	 * Получение списка призов.
	 * [userId] - - необходимо указать Администратору, счет какого Сотрудника нужно получить,
	 *    если не указан, выводится собственные платежные данные Сотрудника.
	 * [deptId] - необходимо заполнить для Владельца, для определения конкретной компании.
	 *    Может быть указан любой отдел компании, бэк сам определит id компании.
	 *    Для всех остальных пользователей поле игнорируется (определяется автоматически).
	 * [payCode] - Необязательный фильтр по типу операции
	 * [isActive] - Необязательный фильтр по активному состоянию операции
	 *
	 * [baseRequest]:
	 *  [filter] - фильтрация по названию приза (необязателен)
	 *  Параметры пагинации [page], [pageSize] - необязательны, по умолчанию 0 и 100 соответственно
	 *  Допустимые поля для сортировки:
	 *      "id",
	 *      "dateOp",
	 *      "payCode",
	 *      "price",
	 *      "isActive",
	 *      "userEntity.firstname",
	 *      "userEntity.lastname",
	 *      "userEntity.dept.name",
	 *      "productEntity.name",
	 *      "productEntity.price",
	 *      "productEntity.count",
	 */
	@PostMapping("get_pays")
	private suspend fun getPaysData(
		@RequestBody request: GetPaysDataRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<List<PayDataResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = payProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportPaysDataResponse() }
		)
	}

}