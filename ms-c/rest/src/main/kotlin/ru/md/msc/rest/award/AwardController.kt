package ru.md.msc.rest.award

import org.springframework.web.bind.annotation.*
import ru.md.msc.domain.award.biz.proc.AwardProcessor
import ru.md.msc.rest.award.mappers.fromTransport
import ru.md.msc.rest.award.mappers.toTransportAwardDetails
import ru.md.msc.rest.award.model.request.CreateAwardRequest
import ru.md.msc.rest.award.model.request.UpdateAwardRequest
import ru.md.msc.rest.award.model.response.AwardDetailsResponse
import ru.md.msc.rest.base.AUTH
import ru.md.msc.rest.base.BaseResponse
import ru.md.msc.rest.base.process
import ru.md.msc.rest.utils.JwtUtils

@RestController
@RequestMapping("award")
class AwardController(
	private val awardProcessor: AwardProcessor,
	private val jwtUtils: JwtUtils,
) {

	@PostMapping("create")
	private suspend fun create(
		@RequestBody request: CreateAwardRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<AwardDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = awardProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwardDetails() }
		)
	}

	@PostMapping("update")
	private suspend fun update(
		@RequestBody request: UpdateAwardRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<AwardDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = awardProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwardDetails() }
		)
	}

}