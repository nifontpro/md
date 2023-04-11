package ru.md.msc.db.dept.service

import ru.md.base.dom.model.RepositoryData
import ru.md.base.dom.model.RepositoryError

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