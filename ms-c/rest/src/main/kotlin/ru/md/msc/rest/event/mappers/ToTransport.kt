package ru.md.msc.rest.event.mappers

import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.msc.domain.event.biz.proc.EventsContext
import ru.md.msc.rest.event.model.response.BaseEventResponse

fun EventsContext.toTransportBaseEvent(): BaseResponse<BaseEventResponse> {
	return baseResponse(baseEvent.toBaseEventResponse())
}

fun EventsContext.toTransportBaseEvents(): BaseResponse<List<BaseEventResponse>> {
	return baseResponse(events.map { it.toBaseEventResponse() })
}