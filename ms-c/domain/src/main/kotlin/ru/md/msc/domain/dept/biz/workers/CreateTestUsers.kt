package ru.md.msc.domain.dept.biz.workers

import ru.md.base_domain.biz.helper.errorDb
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.base_domain.dept.model.Dept
import ru.md.base_domain.user.model.Gender
import ru.md.base_domain.user.model.RoleUser
import ru.md.base_domain.user.model.User
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
				dept = Dept(id = deptDetails.dept.id),
				roles = setOf(RoleUser.USER)
			)
			val userDetails = UserDetails(
				user = user,
				phone = "+7 111-22-33",
				address = "РФ",
				description = "Для теста"
			)
			userService.create(userDetails)
		}
	}

	except {
		log.error(it.message)
		fail(
			errorDb(
				repository = "user",
				violationCode = "user create",
				description = "Ошибка создания тестового сотрудника"
			)
		)
	}
}