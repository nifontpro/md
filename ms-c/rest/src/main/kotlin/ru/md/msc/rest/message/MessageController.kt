package ru.md.msc.rest.message

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import ru.md.base_domain.model.BaseResponse
import ru.md.base_rest.base.authProcess
import ru.md.base_rest.logEndpoint
import ru.md.base_rest.logRequest
import ru.md.base_rest.model.request.AUTH
import ru.md.base_rest.utils.JwtUtils
import ru.md.msc.domain.msg.biz.proc.MessageProcessor
import ru.md.msc.rest.message.mappers.fromTransport
import ru.md.msc.rest.message.mappers.toTransportUserMessagesResponse
import ru.md.msc.rest.message.mappers.toTransportUserMsgResponse
import ru.md.msc.rest.message.model.request.DeleteMessageRequest
import ru.md.msc.rest.message.model.request.GetAuthUserMessageRequest
import ru.md.msc.rest.message.model.request.SendMessageRequest
import ru.md.msc.rest.message.model.request.SetMessageReadStatusRequest
import ru.md.msc.rest.message.model.response.UserMsgResponse

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
		log.info(logEndpoint("send"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("delete"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("get_auth"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("set_read"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = messageProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserMsgResponse() }
		)
	}

	companion object {
		private val log: Logger = LoggerFactory.getLogger(MessageController::class.java)
	}

}