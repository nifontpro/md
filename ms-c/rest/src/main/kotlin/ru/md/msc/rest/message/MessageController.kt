package ru.md.msc.rest.message

import org.springframework.web.bind.annotation.*
import ru.md.base_domain.model.BaseResponse
import ru.md.base_rest.base.authProcess
import ru.md.base_rest.model.request.AUTH
import ru.md.base_rest.utils.JwtUtils
import ru.md.msc.domain.msg.biz.proc.MessageProcessor
import ru.md.msc.rest.message.mappers.fromTransport
import ru.md.msc.rest.message.mappers.toTransportUserMessagesResponse
import ru.md.msc.rest.message.mappers.toTransportUserMsgResponse
import ru.md.msc.rest.message.model.response.UserMsgResponse
import ru.md.msc.rest.message.model.request.DeleteMessageRequest
import ru.md.msc.rest.message.model.request.GetAuthUserMessageRequest
import ru.md.msc.rest.message.model.request.SendMessageRequest
import ru.md.msc.rest.message.model.request.SetMessageReadStatusRequest

@RestController
@RequestMapping("msg")
class MessageController(
	private val messageProcessor: MessageProcessor,
	private val jwtUtils: JwtUtils,
) {

	/**
	 * Отправить сообщение
	 */
	@PostMapping("send")
	private suspend fun sendMessage(
		@RequestBody request: SendMessageRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<UserMsgResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = messageProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserMsgResponse() }
		)
	}

	/**
	 * Удалить сообщение
	 */
	@PostMapping("delete")
	private suspend fun deleteMessage(
		@RequestBody request: DeleteMessageRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<UserMsgResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = messageProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserMsgResponse() }
		)
	}

	/**
	 * Прочитать свои сообщения
	 */
	@PostMapping("get_auth")
	private suspend fun deleteMessage(
		@RequestBody request: GetAuthUserMessageRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<List<UserMsgResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = messageProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserMessagesResponse() }
		)
	}

	/**
	 * Изменить статус прочтения сообщения
	 * read - статус прочтения (по умолчанию true)
	 */
	@PostMapping("set_read")
	private suspend fun deleteMessage(
		@RequestBody request: SetMessageReadStatusRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<UserMsgResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = messageProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserMsgResponse() }
		)
	}

}