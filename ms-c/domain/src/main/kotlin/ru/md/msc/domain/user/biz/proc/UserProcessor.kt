package ru.md.msc.domain.user.biz.proc

import org.springframework.stereotype.Component
import ru.md.base.dom.biz.IBaseProcessor
import ru.md.base.dom.workers.finishOperation
import ru.md.base.dom.workers.initStatus
import ru.md.base.dom.workers.operation
import ru.md.cor.rootChain
import ru.md.msc.domain.user.biz.validate.db.validateOwnerByEmailExist
import ru.md.msc.domain.user.biz.validate.validateUserEmailVerified
import ru.md.msc.domain.user.biz.validate.validateUserFirstnameEmpty
import ru.md.msc.domain.user.biz.workers.createOwner
import ru.md.msc.domain.user.service.UserService

@Component
@Suppress("RemoveExplicitTypeArguments")
class UserProcessor(
	private val userService: UserService
) : IBaseProcessor<UserContext> {

	override suspend fun exec(ctx: UserContext) = businessChain.exec(ctx.also { it.userService = userService })

	companion object {

		private val businessChain = rootChain<UserContext> {
			initStatus()

			operation("Регистрация корневого владельца", UserCommand.CREATE_OWNER) {
				validateUserFirstnameEmpty("Проверка имени пользователя")
				validateUserEmailVerified("Проверка подтвержденности почты")
				validateOwnerByEmailExist("Проверка существования владельца с email")
				createOwner("Создаем владельца")
			}

			finishOperation()
		}.build()
	}
}