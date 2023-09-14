package ru.md.msc.domain.user.biz.proc.operation

import ru.md.base_domain.biz.workers.operation
import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.cor.worker
import ru.md.msc.domain.base.validate.auth.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.validateUserId
import ru.md.msc.domain.base.workers.chain.validateSameOwnerAndAdminModifyUser
import ru.md.msc.domain.base.workers.findModifyUserAndGetRolesAndDeptId
import ru.md.msc.domain.base.workers.image.deleteBaseImagesFromS3
import ru.md.msc.domain.user.biz.proc.UserCommand
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.workers.deleteUser
import ru.md.msc.domain.user.biz.workers.getUserDetailsById
import ru.md.msc.domain.user.biz.workers.userToArchive

fun ICorChainDsl<UserContext>.deleteUserOperation() {
	operation("Удаление профиля сотрудника", UserCommand.DELETE) {
		validateUserId("Проверка userId")
		getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
		findModifyUserAndGetRolesAndDeptId("Получаем профиль для обновления")
		validateSameOwnerAndAdminModifyUser()
		getUserDetailsById("Получаем сотрудника")

		chain {
			on { deleteForever }
			deleteUser("Удаляем профиль сотрудника")
			worker("Подготовка к удалению изображений") { baseImages = userDetails.user.images }
			deleteBaseImagesFromS3("Удаляем все изображения")
		}

		chain {
			on { !deleteForever }
			userToArchive("Архивируем профиль сотрудника")
			worker("Изменяем статус") {
				userDetails = userDetails.copy(user = userDetails.user.copy(archive = true))
			}
		}
	}
}