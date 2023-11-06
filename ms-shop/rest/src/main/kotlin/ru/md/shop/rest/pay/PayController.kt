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

	/**
	 * Выдать приз Сотруднику Администратором.
	 */
	@PostMapping("give_admin")
	private suspend fun giveProductAdmin(
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

}