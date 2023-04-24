package ru.md.msc.domain.user.biz.proc

import org.springframework.stereotype.Component
import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.IBaseProcessor
import ru.md.msc.domain.base.validate.db.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.db.validateAuthDeptDownLevel
import ru.md.msc.domain.base.validate.db.validateAuthDeptLevel
import ru.md.msc.domain.base.validate.db.validateAuthUserLevel
import ru.md.msc.domain.base.validate.validateAdminRole
import ru.md.msc.domain.base.validate.validateImageId
import ru.md.msc.domain.base.validate.validateUserId
import ru.md.msc.domain.base.workers.finishOperation
import ru.md.msc.domain.base.workers.initStatus
import ru.md.msc.domain.base.workers.operation
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.user.biz.validate.db.validateOwnerByEmailExist
import ru.md.msc.domain.user.biz.validate.validateUserFirstnameEmpty
import ru.md.msc.domain.user.biz.validate.validateUserRoles
import ru.md.msc.domain.user.biz.workers.*
import ru.md.msc.domain.user.service.UserService

@Component
class UserProcessor(
	private val userService: UserService,
	private val deptService: DeptService
) : IBaseProcessor<UserContext> {

	override suspend fun exec(ctx: UserContext) = businessChain.exec(ctx.also {
		it.userService = userService
		it.deptService = deptService
	})

	companion object {

		private val businessChain = rootChain<UserContext> {
			initStatus()

			operation("Регистрация корневого владельца", UserCommand.CREATE_OWNER) {
				validateUserFirstnameEmpty("Проверка имени пользователя")
				validateOwnerByEmailExist("Проверка существования владельца с email")
				trimFieldUserDetails("Очищаем поля")
				createOwner("Создаем владельца")
			}

			operation("Создание профиля сотрудника", UserCommand.CREATE) {
				validateUserFirstnameEmpty("Проверка имени пользователя")
				validateUserRoles("Проверка ролей")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAdminRole("Проверка наличия прав Администратора")
				validateAuthDeptLevel("Проверка доступа к отделу")
				trimFieldUserDetails("Очищаем поля")
				createUser("Создаем профиль сотрудника")
			}

			operation("Обновление профиля сотрудника", UserCommand.UPDATE) {
				validateUserFirstnameEmpty("Проверка имени пользователя")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")

				chain {
					on { userId != authUser.id } // Если запрос не собственного профиля:
					validateAdminModifyUserByRole() // Тогда должны быть права Администратора
				}

				trimFieldUserDetails("Очищаем поля")
				updateUser("Обновляем профиль сотрудника")
			}

			operation("Удаление профиля сотрудника", UserCommand.DELETE) {
				validateUserId("Проверка userId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAdminModifyUserByRole()
				getUserDetailsById("Получаем сотрудника")
				deleteUser("Удаляем сотрудника")
			}

			operation("Получение профилей пользователя", UserCommand.GET_PROFILES) {
				getProfiles("Получаем доступные профили")
			}

			operation("Получение сотрудников отдела", UserCommand.GET_BY_DEPT) {
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptLevel("Проверка доступа к отделу")
				getUsersByDept("Получаем сотрудников")
			}

			operation("Получение профиля сотрудника", UserCommand.GET_BY_ID_DETAILS) {
				validateUserId("Проверка userId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")

				chain {
					on { userId != authUser.id } // Если запрос не собственного профиля:
					validateAdminRole("Проверка наличия прав Администратора")
					validateAuthUserLevel("Проверка доступа к сотруднику")
				}

				getUserDetailsById("Получаем сотрудника")
			}

			operation("Добавление изображения", UserCommand.IMG_ADD) {
				worker("Получение id сущности") { userId = fileData.entityId; authId = userId }
				validateUserId("Проверка userId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				addUserImage("Добавляем изображение")
			}

			operation("Обновление изображения", UserCommand.IMG_UPDATE) {
				validateImageId("Проверка imageId")
				worker("Получение id сущности") { userId = fileData.entityId; authId = userId }
				validateUserId("Проверка userId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				updateUserImage("Обновляем изображение")
			}

			operation("Удаление изображения", UserCommand.IMG_DELETE) {
				validateUserId("Проверка userId")
				validateImageId("Проверка imageId")
				worker("Подготовка") { authId = userId }
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				deleteUserImage("Удаляем изображение")
			}

			finishOperation()
		}.build()

		/**
		 * Проверка доступа авторизованного пользователя с правами Администратора
		 * к обновлению/удалению профиля сотрудника в зависимости от его роли.
		 * Администратор имеет право над сотрудниками в своем и нижестоящих отделах
		 * и имеет право над Администраторами нижестоящих отделов
		 */
		private fun ICorChainDsl<UserContext>.validateAdminModifyUserByRole() {

			validateAdminRole("Проверка наличия прав Администратора")
			findUpdateUser("Получаем профиль для обновления")
			chain {
				on { !isUpdateUserHasAdminRole } // Обновляемый без прав ADMIN
				validateAuthDeptLevel("Проверка доступа к отделу")
			}
			chain {
				on { isUpdateUserHasAdminRole } // Обновляемый имеет роль ADMIN
				validateAuthDeptDownLevel("Проверка доступа к нижестоящему отделу")
			}
		}

	}
}