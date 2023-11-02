package ru.md.shop.rest.pay

import org.springframework.web.bind.annotation.*
import ru.md.base_domain.model.BaseResponse
import ru.md.base_rest.base.authProcess
import ru.md.base_rest.model.request.AUTH
import ru.md.base_rest.utils.JwtUtils
import ru.md.shop.domain.pay.biz.proc.PayProcessor
import ru.md.shop.rest.pay.mappers.fromTransport
import ru.md.shop.rest.pay.mappers.toTransportPayDataResponse
import ru.md.shop.rest.pay.mappers.toTransportUserPayResponse
import ru.md.shop.rest.pay.model.request.GetUserPayRequest
import ru.md.shop.rest.pay.model.request.PayProductRequest
import ru.md.shop.rest.pay.model.response.PayDataResponse
import ru.md.shop.rest.pay.model.response.UserPayResponse

@RestController
@RequestMapping("pay")
class PayController(
	private val payProcessor: PayProcessor,
	private val jwtUtils: JwtUtils,
) {

	@PostMapping("user_pay")
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

}