package ru.md.msc.domain.user.biz.proc

import org.springframework.stereotype.Component
import ru.md.cor.rootChain
import ru.md.msc.domain.base.biz.IBaseProcessor
import ru.md.msc.domain.base.workers.finishOperation
import ru.md.msc.domain.base.workers.initStatus
import ru.md.msc.domain.base.workers.operation
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.user.biz.validate.db.validateOwnerByEmailExist
import ru.md.msc.domain.user.biz.validate.validateUserFirstnameEmpty
import ru.md.msc.domain.user.biz.workers.createOwner
import ru.md.msc.domain.user.service.UserService

@Component
@Suppress("RemoveExplicitTypeArguments")
class UserProcessor(
	private val userService: UserService,
	private val deptService: DeptService
) : IBaseProcessor<UserContext> {

	override suspend fun exec(ctx: UserContext) = businessChain.exec(ctx.also {
		it.userService = userService
		it.deptService = deptService
	})

	companion object {

		private val businessChain = rootChain<UserContext> {
			initStatus()

			operation("Регистрация корневого владельца", UserCommand.CREATE_OWNER) {
				validateUserFirstnameEmpty("Проверка имени пользователя")
				validateOwnerByEmailExist("Проверка существования владельца с email")
				createOwner("Создаем владельца")
			}

			finishOperation()
		}.build()
	}
}