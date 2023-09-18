package ru.md.msc.rest.medal

import org.springframework.web.bind.annotation.*
import ru.md.base_domain.rest.BaseResponse
import ru.md.base_rest.authProcess
import ru.md.base_rest.model.AUTH
import ru.md.base_rest.utils.JwtUtils
import ru.md.msc.domain.medal.biz.proc.MedalProcessor
import ru.md.msc.rest.medal.mappers.fromTransport
import ru.md.msc.rest.medal.mappers.toTransportMedalDetails
import ru.md.msc.rest.medal.model.request.CreateMedalRequest
import ru.md.msc.rest.medal.model.request.GetMedalByIdRequest
import ru.md.msc.rest.medal.model.request.UpdateMedalRequest
import ru.md.msc.rest.medal.model.response.MedalDetailsResponse

@RestController
@RequestMapping("medal")
class MedalController(
	private val medalProcessor: MedalProcessor,
	private val jwtUtils: JwtUtils,
) {

	@PostMapping("create")
	private suspend fun create(
		@RequestBody request: CreateMedalRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<MedalDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = medalProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportMedalDetails() }
		)
	}

	@PostMapping("get_id")
	private suspend fun getById(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetMedalByIdRequest
	): BaseResponse<MedalDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = medalProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportMedalDetails() }
		)
	}

	@PostMapping("update")
	private suspend fun update(
		@RequestBody request: UpdateMedalRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<MedalDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = medalProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportMedalDetails() }
		)
	}

}