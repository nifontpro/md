package ru.md.msc.domain.msg.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.cor.rootChain
import ru.md.msc.domain.base.validate.auth.getAuthUserAndVerifyEmail
import ru.md.base_domain.biz.validate.validateUserId
import ru.md.msc.domain.base.workers.msg.sendMessage
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.msg.biz.validate.validateDeleteMessage
import ru.md.msc.domain.msg.biz.validate.validateMessageEmpty
import ru.md.msc.domain.msg.biz.validate.validateMessageId
import ru.md.msc.domain.msg.biz.validate.validateModifyMessage
import ru.md.msc.domain.msg.biz.workers.*
import ru.md.msc.domain.msg.service.MessageService
import ru.md.msc.domain.user.service.UserService

@Component
class MessageProcessor(
	private val userService: UserService,
	private val deptService: DeptService,
	private val messageService: MessageService
) : IBaseProcessor<MessageContext> {

	override suspend fun exec(ctx: MessageContext) = businessChain.exec(ctx.also {
		it.userService = userService
		it.deptService = deptService
		it.messageService = messageService
	})

	companion object {

		private val businessChain = rootChain {
			initStatus()

			operation("Отправить сообщение", MessageCommand.SEND) {
				validateUserId("Проверка userId")
				validateMessageEmpty("Проверка непустого сообщения")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				prepareSendMessageToUser("Подготовка сообщения к отправке сотруднику")
				sendMessage("Отправляем сообщение")
			}

			operation("Удалить сообщение", MessageCommand.DELETE) {
				validateMessageId("Проверка messageId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getMessageById("Получаем сообщение")
				validateDeleteMessage("Проверка доступа для удаления сообщения")
				deleteMessage("Удаление сообщения")
			}

			operation("Получить сообщения сотрудника", MessageCommand.GET_AUTH_USER) {
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getMessageByAuthUser("Получаем сообщения авторизованного сотрудника")
			}

			operation("Установить статус прочтения сообщения", MessageCommand.SET_READ_STATUS) {
				validateMessageId("Проверка messageId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getMessageById("Получаем сообщение")
				validateModifyMessage("Проверка доступа к сообщению")
				setMessageReadStatus("Изменяем статус сообщения")
			}

			finishOperation()
		}.build()

	}
}