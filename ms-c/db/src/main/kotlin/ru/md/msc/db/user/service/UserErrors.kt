package ru.md.msc.db.user.service

import ru.md.base.dom.model.RepositoryData
import ru.md.base.dom.model.RepositoryError

class UserErrors {
	companion object {
		private const val REPO = "user"

		fun createOwner() = RepositoryData.error(
			error = RepositoryError(
				repository = REPO,
				violationCode = "owner create",
				description = "Ошибка создания профиля владельца"
			)
		)

		fun getOwnerByEmailExist() = RepositoryData.error(
			error = RepositoryError(
				repository = REPO,
				violationCode = "get owner by email exist",
				description = "Ошибка при проверке существования владельца по email"
			)
		)

	}
}