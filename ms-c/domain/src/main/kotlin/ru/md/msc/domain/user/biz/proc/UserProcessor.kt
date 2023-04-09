package ru.md.msc.domain.user.biz.proc

import org.springframework.stereotype.Component
import ru.md.cor.base.biz.IBaseProcessor
import ru.md.cor.base.workers.finishOperation
import ru.md.cor.base.workers.initStatus
import ru.md.cor.base.workers.operation
import ru.md.cor.rootChain
import ru.md.cor.worker

//@Suppress("RemoveExplicitTypeArguments")
@Component
class UserProcessor : IBaseProcessor<UserContext> {

	override suspend fun exec(ctx: UserContext) = businessChain.exec(ctx)

	companion object {

		private val businessChain = rootChain<UserContext> {
			initStatus("Инициализация статуса")

			operation("Создать профиль сотрудника", UserCommand.CREATE_OWNER) {
				worker("Подготовка полей к авторизации") {
					user = user.copy(patronymic = "Your")
				}
			}
			finishOperation()
		}.build()
	}
}