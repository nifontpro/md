package ru.md.msc.domain.dept.biz.proc

import org.springframework.stereotype.Component
import ru.md.cor.rootChain
import ru.md.msc.domain.base.biz.IBaseProcessor
import ru.md.msc.domain.base.validate.db.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.validateAdminRole
import ru.md.msc.domain.base.workers.finishOperation
import ru.md.msc.domain.base.workers.initStatus
import ru.md.msc.domain.base.workers.operation
import ru.md.msc.domain.dept.biz.validate.db.validateAuthDeptLevel
import ru.md.msc.domain.dept.biz.workers.createDept
import ru.md.msc.domain.dept.biz.workers.createTestUsers
import ru.md.msc.domain.dept.biz.workers.getSubtreeDepts
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

		private val businessChain = rootChain {
			initStatus()

			operation("Создать отдел", DeptCommand.CREATE) {
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAdminRole("Проверка наличия прав Администратора")
				validateAuthDeptLevel("Проверка доступа к отделу")
				createDept("Создаем отдел")
				createTestUsers("Создаем тестовых сотрудников")
			}

			operation("Получить поддерево отделов", DeptCommand.GET_DEPTS_TREE) {
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getSubtreeDepts("Получаем поддерево отделов")
			}

			finishOperation()
		}.build()
	}
}