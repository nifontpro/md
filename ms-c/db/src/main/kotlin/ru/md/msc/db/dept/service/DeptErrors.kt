package ru.md.msc.db.dept.service

import ru.md.msc.domain.base.model.RepositoryData
import ru.md.msc.domain.base.model.RepositoryError

class DeptErrors {
	companion object {
		private const val REPO = "dept"

		fun createDept() = RepositoryData.error(
			error = RepositoryError(
				repository = REPO,
				violationCode = "dept create",
				description = "Ошибка создания отдела"
			)
		)

		fun getDeptAuth() = RepositoryData.error(
			error = RepositoryError(
				repository = REPO,
				violationCode = "dept auth",
				description = "Ошибка проверки прав доступа отдела"
			)
		)

		fun getError() = RepositoryData.error(
			error = RepositoryError(
				repository = REPO,
				violationCode = "get depts",
				description = "Ошибка получения отделов"
			)
		)

		fun deleteError() = RepositoryData.error(
			error = RepositoryError(
				repository = REPO,
				violationCode = "delete",
				description = "Ошибка удаления отдела"
			)
		)

		fun notFound() = RepositoryData.error(
			error = RepositoryError(
				repository = REPO,
				violationCode = "not found",
				description = "Отдел не найден"
			)
		)
	}
}