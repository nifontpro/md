package ru.md.msc.rest.event

import org.springframework.web.bind.annotation.*
import ru.md.base_domain.rest.BaseResponse
import ru.md.base_rest.authProcess
import ru.md.base_rest.model.AUTH
import ru.md.base_rest.utils.JwtUtils
import ru.md.msc.domain.event.biz.proc.EventProcessor
import ru.md.msc.rest.event.mappers.fromTransport
import ru.md.msc.rest.event.mappers.toTransportBaseEvent
import ru.md.msc.rest.event.mappers.toTransportBaseEvents
import ru.md.msc.rest.event.mappers.toTransportShortEvents
import ru.md.msc.rest.event.model.request.*
import ru.md.msc.rest.event.model.response.BaseEventResponse
import ru.md.msc.rest.event.model.response.ShortEventResponse

@RestController
@RequestMapping("event")
class EventController(
	private val eventProcessor: EventProcessor,
	private val jwtUtils: JwtUtils,
) {

	@PostMapping("add_user")
	private suspend fun addUserEvent(
		@RequestBody request: AddUserEventRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<BaseEventResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = eventProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseEvent() }
		)
	}

	@PostMapping("add_dept")
	private suspend fun addDeptEvent(
		@RequestBody request: AddDeptEventRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<BaseEventResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = eventProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseEvent() }
		)
	}

	/**
	 * Получить все события сотрудников и отделов
	 * с текущего дня года по кругу.
	 * Пагинация.
	 * Сортировка внутренняя (По дню от текущего и названию сущности).
	 */
	@PostMapping("get_all")
	private suspend fun getEvents(
		@RequestBody request: GetAllEventsRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<List<BaseEventResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = eventProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseEvents() }
		)
	}

	@PostMapping("get_user")
	private suspend fun getUserEvents(
		@RequestBody request: GetUserEventsRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<List<ShortEventResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = eventProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportShortEvents() }
		)
	}

	@PostMapping("get_dept")
	private suspend fun getDeptEvents(
		@RequestBody request: GetDeptEventsRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<List<ShortEventResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = eventProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportShortEvents() }
		)
	}

}