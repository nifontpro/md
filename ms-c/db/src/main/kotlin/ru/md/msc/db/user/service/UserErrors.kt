package ru.md.msc.db.user.service

import ru.md.msc.domain.base.model.RepositoryData
import ru.md.msc.domain.base.model.RepositoryError

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

		fun create() = RepositoryData.error(
			error = RepositoryError(
				repository = REPO,
				violationCode = "create",
				description = "Ошибка создания профиля сотрудника"
			)
		)

		fun getOwnerByEmailExist() = RepositoryData.error(
			error = RepositoryError(
				repository = REPO,
				violationCode = "get owner by email exist",
				description = "Ошибка при проверке существования владельца по email"
			)
		)

		fun userNotFound() = RepositoryData.error(
			error = RepositoryError(
				repository = REPO,
				violationCode = "not found",
				description = "Сотрудник не найден"
			)
		)

		fun getError() = RepositoryData.error(
			error = RepositoryError(
				repository = REPO,
				violationCode = "get error",
				description = "Ошибка чтения"
			)
		)

	}
}