package ru.md.msc.domain.user.biz.proc

import org.springframework.stereotype.Component
import ru.md.cor.ICorChainDsl
import ru.md.cor.chain
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.IBaseProcessor
import ru.md.msc.domain.base.validate.*
import ru.md.msc.domain.base.validate.db.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.db.validateAuthDeptDownLevel
import ru.md.msc.domain.base.validate.db.validateAuthDeptLevel
import ru.md.msc.domain.base.validate.db.validateAuthUserLevel
import ru.md.msc.domain.base.workers.*
import ru.md.msc.domain.base.workers.chain.deleteS3ImageOnFailingChain
import ru.md.msc.domain.base.workers.chain.validateRequiredPageParamsChain
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.image.repository.S3Repository
import ru.md.msc.domain.user.biz.validate.db.validateOwnerByEmailExist
import ru.md.msc.domain.user.biz.validate.validateCreateUserRoles
import ru.md.msc.domain.user.biz.validate.validateUserFirstnameEmpty
import ru.md.msc.domain.user.biz.workers.*
import ru.md.msc.domain.user.biz.workers.sort.setUsersBySubdeptsValidSortedFields
import ru.md.msc.domain.user.biz.workers.sort.setUsersValidSortedFields
import ru.md.msc.domain.user.service.UserService

@Component
class UserProcessor(
	private val userService: UserService,
	private val deptService: DeptService,
	private val s3Repository: S3Repository
) : IBaseProcessor<UserContext> {

	override suspend fun exec(ctx: UserContext) = businessChain.exec(ctx.also {
		it.userService = userService
		it.deptService = deptService
		it.s3Repository = s3Repository
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
				validateDeptId("Проверка deptId")
				validateCreateUserRoles("Проверка ролей")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				findCreateUserAdminRole("Сканируем роль ADMIN у нового сотрудника")
				validateAdminModifyUserByRoleChain()
				trimFieldUserDetails("Очищаем поля")
				createUser("Создаем профиль сотрудника")
			}

			operation("Обновление профиля сотрудника", UserCommand.UPDATE) {
				validateUserFirstnameEmpty("Проверка имени пользователя")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")

				chain {
					on { userId != authUser.id } // Если запрос не собственного профиля:
					findModifyUserAndGetRolesAndDeptId("Получаем профиль для обновления")
					validateAdminModifyUserByRoleChain() // Тогда должны быть права Администратора
				}

				trimFieldUserDetails("Очищаем поля")
				updateUser("Обновляем профиль сотрудника")
			}

			operation("Удаление профиля сотрудника", UserCommand.DELETE) {
				validateUserId("Проверка userId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				findModifyUserAndGetRolesAndDeptId("Получаем профиль для обновления")
				validateAdminModifyUserByRoleChain()
				getUserDetailsById("Получаем сотрудника")
				deleteUser("Удаляем сотрудника")
				worker("Подготовка к удалению изображений") { baseImages = userDetails.user.images }
				deleteBaseImagesFromS3("Удаляем все изображения")
			}

			operation("Получение профилей пользователя", UserCommand.GET_PROFILES) {
				getProfiles("Получаем доступные профили")
			}

			operation("Получение сотрудников отдела", UserCommand.GET_BY_DEPT) {
				validateRequiredPageParamsChain()
				setUsersValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptLevel("Проверка доступа к отделу")
				getUsersByDept("Получаем сотрудников")
			}

			operation("Получение сотрудников всех подотделов", UserCommand.GET_BY_SUB_DEPTS) {
				validateRequiredPageParamsChain()
				setUsersBySubdeptsValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptLevel("Проверка доступа к отделу")
				getUsersBySubDepts("Получаем сотрудников подотделов")
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
				worker("Получение id сущности") { userId = fileData.entityId }
				validateUserId("Проверка userId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				addUserImageToS3("Сохраняем изображение в s3")
				addUserImageToDb("Сохраняем изображение в БД")
				deleteS3ImageOnFailingChain()
			}

			operation("Удаление изображения", UserCommand.IMG_DELETE) {
				validateUserId("Проверка userId")
				validateImageId("Проверка imageId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				deleteUserImageFromDb("Удаляем изображение из БД")
				deleteBaseImageFromS3("Удаляем изображение из s3")
			}

			finishOperation()
		}.build()

		/**
		 * Проверка доступа авторизованного пользователя с правами Администратора
		 * к созданию/обновлению/удалению профиля сотрудника в зависимости от его роли.
		 * Администратор имеет право над сотрудниками в своем и нижестоящих отделах
		 * и имеет право над Администраторами нижестоящих отделов
		 */
		private fun ICorChainDsl<UserContext>.validateAdminModifyUserByRoleChain() {
			validateAdminRole("Проверка наличия прав Администратора")
			chain {
				on { !isModifyUserHasAdminRole } // Обновляемый без прав ADMIN
				validateAuthDeptLevel("Проверка доступа к отделу")
			}
			chain {
				on { isModifyUserHasAdminRole } // Обновляемый имеет роль ADMIN
				validateAuthDeptDownLevel("Проверка доступа к нижестоящему отделу")
			}
		}

	}
}