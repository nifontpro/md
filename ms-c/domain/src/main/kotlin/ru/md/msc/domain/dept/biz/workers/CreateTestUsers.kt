package ru.md.msc.domain.dept.biz.workers

import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.ContextState
import ru.md.msc.domain.base.helper.errorDb
import ru.md.msc.domain.base.helper.fail
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.user.model.Gender
import ru.md.msc.domain.user.model.RoleUser
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails

fun ICorChainDsl<DeptContext>.createTestUsers(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		if (!addTestUser) return@handle

		for (i in 1..10) {
			val user = User(
				firstname = "Сотрудник $i",
				patronymic = "Тестовый",
				lastname = "Отдела ${dept.name}",
				post = "Нет",
				gender = Gender.UNDEF,
				dept = Dept(id = deptDetails.dept?.id ?: 0),
				roles = setOf(RoleUser.USER)
			)
			val userDetails = UserDetails(
				user = user,
				phone = "+7 111-22-33",
				address = "РФ",
				description = "Для теста"
			)

			try {
				userService.create(userDetails)
			} catch (e: Exception) {
				fail(
					errorDb(
						repository = "user",
						violationCode = "user create",
						description = "Ошибка создания тестового сотрудника"
					)
				)
				return@handle
			}
		}
	}
}