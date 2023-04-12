package ru.md.msc.domain.dept.biz.proc

import org.springframework.stereotype.Component
import ru.md.cor.rootChain
import ru.md.msc.domain.base.biz.IBaseProcessor
import ru.md.msc.domain.base.validate.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.workers.finishOperation
import ru.md.msc.domain.base.workers.initStatus
import ru.md.msc.domain.base.workers.operation
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.user.service.UserService

@Component
class DeptProcessor(
	private val userService: UserService,
	private val deptService: DeptService
) : IBaseProcessor<DeptContext> {

	override suspend fun exec(ctx: DeptContext) = businessChain.exec(ctx.also {
		it.userService = userService
		it.deptService = deptService
	})

	companion object {

		private val businessChain = rootChain<DeptContext> {
			initStatus()

			operation("Создать отдел", DeptCommand.CREATE) {
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя")
			}

			finishOperation()
		}.build()
	}
}