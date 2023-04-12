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
	}
}