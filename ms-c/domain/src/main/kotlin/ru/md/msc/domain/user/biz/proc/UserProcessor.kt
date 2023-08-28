package ru.md.msc.domain.user.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.validate.validateSortedFields
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.cor.chain
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.validate.validateAwardId
import ru.md.msc.domain.base.validate.auth.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.auth.validateAuthDeptTopLevelForView
import ru.md.msc.domain.base.validate.auth.validateAuthUserLevel
import ru.md.msc.domain.base.validate.validateAdminRole
import ru.md.msc.domain.base.validate.validateDeptId
import ru.md.msc.domain.base.validate.validateImageId
import ru.md.msc.domain.base.validate.validateUserId
import ru.md.msc.domain.base.workers.chain.*
import ru.md.msc.domain.base.workers.findModifyUserAndGetRolesAndDeptId
import ru.md.msc.domain.base.workers.image.addImageToS3
import ru.md.msc.domain.base.workers.image.deleteBaseImageFromS3
import ru.md.msc.domain.base.workers.image.deleteBaseImagesFromS3
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.s3.repository.S3Repository
import ru.md.msc.domain.user.biz.validate.db.validateOwnerByEmailExist
import ru.md.msc.domain.user.biz.validate.validateCreateUserRoles
import ru.md.msc.domain.user.biz.validate.validateUserFirstnameEmpty
import ru.md.msc.domain.user.biz.workers.*
import ru.md.msc.domain.user.biz.workers.sort.setUsersBySubdeptsValidSortedFields
import ru.md.msc.domain.user.biz.workers.sort.setUsersWithAwardCountValidSortedFields
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
				validateUserId("Проверка userId")
				validateUserFirstnameEmpty("Проверка имени пользователя")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateSameAndAdminModifyUser() // Проверка модификации собственного профиля или Администратором
				trimFieldUserDetails("Очищаем поля")
				updateUser("Обновляем профиль сотрудника")
			}

			operation("Удаление профиля сотрудника", UserCommand.DELETE) {
				validateUserId("Проверка userId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				findModifyUserAndGetRolesAndDeptId("Получаем профиль для обновления")
				validateSameOwnerAndAdminModifyUser()
				getUserDetailsById("Получаем сотрудника")
				deleteUser("Удаляем сотрудника")
				worker("Подготовка к удалению изображений") { baseImages = userDetails.user.images }
				deleteBaseImagesFromS3("Удаляем все изображения")
			}

			operation("Получение профилей пользователя", UserCommand.GET_PROFILES) {
				getProfiles("Получаем доступные профили")
			}

			operation("Получение сотрудников всех подотделов", UserCommand.GET_BY_SUB_DEPTS) {
				validateDeptId("Проверка deptId")
				validatePageParamsChain()
				setUsersBySubdeptsValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getUsersBySubDepts("Получаем сотрудников подотделов")
			}

			operation("Получение сотрудников отделов без награды", UserCommand.GET_BY_SUB_DEPTS_EXCLUDE_AWARD) {
				validateDeptId("Проверка deptId")
				validateAwardId("Проверяем awardId")
				validatePageParamsChain()
				setUsersBySubdeptsValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getUsersByDeptsExclude("Получаем сотрудников отделов без данной награды")
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
				validateSameAndAdminModifyUser() // Проверка модификации собственного профиля или Администратором
				prepareUserImagePrefixUrl("Получаем префикс изображения")
				addImageToS3("Сохраняем изображение в s3")
				addUserImageToDb("Сохраняем изображение в БД")
				updateUserMainImage("Обновление основного изображения")
				deleteS3ImageOnFailingChain()
			}

			operation("Удаление изображения", UserCommand.IMG_DELETE) {
				validateUserId("Проверка userId")
				validateImageId("Проверка imageId")
				// Авторизация
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				deleteUserImageFromDb("Удаляем изображение из БД")
				deleteBaseImageFromS3("Удаляем изображение из s3")
				updateUserMainImage("Обновление основного изображения")
			}

			operation("Подсчет количества сотрудников по полам", UserCommand.GENDER_COUNT_BY_DEPTS) {
				validateDeptId("Проверка deptId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getGenderCountByDept("Подсчет количества по полам")
			}

			operation("Сотрудники с активностью", UserCommand.GET_WITH_ACTIVITY) {
				validateDeptId("Проверка deptId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getUsersWithActivityByDept("Получаем сотрудников с активностью")
			}

			operation("Сотрудники с наградами", UserCommand.GET_WITH_AWARDS) {
				validateDeptId("Проверка deptId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getUsersWithAwardsByDept("Получаем сотрудников с наградами")
			}

			operation("Количество сотрудников с наградами", UserCommand.GET_WITH_AWARD_COUNT) {
				validateDeptId("Проверка deptId")
				setUsersWithAwardCountValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getUsersWithAwardCountByDept("Получаем сотрудников с количеством наград")
			}

			operation("Установить главные изображения у всех", UserCommand.SET_MAIN_IMG) {
				setMainImagesForUsers("Устанавливаем главные изображения для всех сотрудников")
			}

			operation("Сохранить настройки", UserCommand.SAVE_SETTINGS) {
				validateUserId("Проверка userId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				saveUserSettings("Сохраняем настройки")
			}

			operation("Получить настройки", UserCommand.GET_SETTINGS) {
				validateUserId("Проверка userId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getUserSettings("Получаем настройки")
			}

			finishOperation()
		}.build()

	}
}