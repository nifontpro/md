package ru.md.msc.domain.user.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.validate.chain.validatePageParamsChain
import ru.md.base_domain.biz.validate.validateAdminRole
import ru.md.base_domain.biz.validate.validateAuthDeptLevel
import ru.md.base_domain.biz.validate.validateAuthDeptTopLevelForView
import ru.md.base_domain.biz.validate.validateSortedFields
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.base_domain.dept.biz.validate.validateDeptId
import ru.md.base_domain.dept.biz.workers.chain.findCompanyDeptIdByOwnerOrAuthUserChain
import ru.md.base_domain.dept.service.BaseDeptService
import ru.md.base_domain.image.biz.chain.deleteS3ImageOnFailingChain
import ru.md.base_domain.image.biz.validate.validateImageId
import ru.md.base_domain.image.biz.workers.addImageToS3
import ru.md.base_domain.image.biz.workers.deleteBaseImageFromS3
import ru.md.base_domain.s3.repo.BaseS3Repository
import ru.md.base_domain.user.biz.validate.validateUserId
import ru.md.base_domain.user.biz.workers.getAuthUserAndVerifyEmail
import ru.md.base_domain.user.biz.workers.getDeptIdByUserId
import ru.md.base_domain.user.service.BaseUserService
import ru.md.cor.chain
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.validate.validateAwardId
import ru.md.msc.domain.base.workers.chain.validateAdminModifyUserByRoleChain
import ru.md.msc.domain.base.workers.chain.validateSameAndAdminModifyUser
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.event.service.EventService
import ru.md.msc.domain.user.biz.proc.operation.deleteUserOperation
import ru.md.msc.domain.user.biz.validate.*
import ru.md.msc.domain.user.biz.validate.db.validateOwnerByEmailExist
import ru.md.msc.domain.user.biz.workers.*
import ru.md.msc.domain.user.biz.workers.event.addBaseEventToUserDetails
import ru.md.msc.domain.user.biz.workers.event.addOrUpdateUserEvents
import ru.md.msc.domain.user.biz.workers.excel.addFromExcel
import ru.md.msc.domain.user.biz.workers.service.addGender
import ru.md.msc.domain.user.biz.workers.sort.setUsersBySubdeptsValidSortedFields
import ru.md.msc.domain.user.biz.workers.sort.setUsersWithAwardCountValidSortedFields
import ru.md.msc.domain.user.service.GenderService
import ru.md.msc.domain.user.service.UserService

@Component
class UserProcessor(
	private val baseDeptService: BaseDeptService,
	private val baseUserService: BaseUserService,
	private val baseS3Repository: BaseS3Repository,
	private val userService: UserService,
	private val deptService: DeptService,
	private val eventService: EventService,
	private val genderService: GenderService,
) : IBaseProcessor<UserContext> {

	override suspend fun exec(ctx: UserContext) = businessChain.exec(ctx.also {
		it.baseDeptService = baseDeptService
		it.baseUserService = baseUserService
		it.baseS3Repository = baseS3Repository
		it.userService = userService
		it.deptService = deptService
		it.eventService = eventService
		it.genderService = genderService
	})

	companion object {

		private val businessChain = rootChain<UserContext> {
			initStatus()

			operation("Регистрация корневого владельца", UserCommand.CREATE_OWNER) {
				validateUserFirstnameEmpty("Проверка имени пользователя")
				validateUserLastnameEmpty("Проверка фамилии пользователя")
				validateOwnerByEmailExist("Проверка существования владельца с email")
				trimFieldUserDetails("Очищаем поля")
				createOwner("Создаем владельца")
			}

			operation("Создание профиля сотрудника", UserCommand.CREATE) {
				validateUserFirstnameEmpty("Проверка имени пользователя")
				validateUserLastnameEmpty("Проверка фамилии пользователя")
				validateUserEmailFormat("Проверяем email")
				validateDeptId("Проверка deptId")
				validateCreateUserRoles("Проверка ролей")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				findCreateUserAdminRole("Сканируем роль ADMIN у нового сотрудника")
				validateUserEmailExist("Проверка наличия сотрудника с почтой")
				validateAdminModifyUserByRoleChain()
				trimFieldUserDetails("Очищаем поля")
				createUser("Создаем профиль сотрудника")
			}

			operation("Добавление сотрудников из таблицы", UserCommand.ADD_FROM_EXCEL) {
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAdminRole("Проверка наличия прав Администратора")
				findCompanyDeptIdByOwnerOrAuthUserChain()
				addFromExcel("Добавляем сотрудников")
			}

			operation("Обновление профиля сотрудника", UserCommand.UPDATE) {
				validateUserId("Проверка userId")
				validateUserFirstnameEmpty("Проверка имени пользователя")
				validateUserLastnameEmpty("Проверка фамилии пользователя")
				validateUserEmailFormat("Проверяем email")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateSameAndAdminModifyUser() // Проверка модификации собственного профиля или Администратором
				trimFieldUserDetails("Очищаем поля")
				chain {
					// чтоб не возникала ошибка, если почта уже установлена, даже повторяющаяся
					on { modifyUser.authEmail?.lowercase()?.trim() != user.authEmail?.lowercase() }
					validateUserEmailExist("Проверка наличия Сотрудника с почтой")
				}
				chain {
					// При перемещении сотрудника в другой отдел
					on { user.dept?.id != 0L && user.dept?.id != modifyUser.dept?.id }
					validateAdminRole("Проверяем наличие прав Администратора")
					worker("target deptId") { deptId = user.dept?.id ?: 0 }
					validateAuthDeptLevel("Проверяем доступ к целевому отделу")
				}
				addOrUpdateUserEvents("Обновляем события Сотрудника")
				updateUser("Обновляем профиль Сотрудника")
			}

			deleteUserOperation()

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
					getDeptIdByUserId("Находим отдел для авторизации")
					validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				}
				getUserDetailsById("Получаем сотрудника")
				addBaseEventToUserDetails("Добавляем основные события")
			}

			operation("Добавление изображения", UserCommand.IMG_ADD) {
				worker("Получение id сущности") { userId = imageData.entityId }
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
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateSameAndAdminModifyUser() // Проверка модификации собственного профиля или Администратором
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

			operation("Проверка, имеет ли сотрудник роль Владельца", UserCommand.CHECK_HAS_OWNER_ROLE) {
				validateUserId("Проверка userId")
				checkUserHasOwnerRole("Проверяем наличие роли Владельца")
			}

			operation("Установить главные изображения у всех", UserCommand.SRV_SET_MAIN_IMG) {
				setMainImagesForUsers("Устанавливаем главные изображения для всех сотрудников")
			}

			operation("Создаем базу мужских имен", UserCommand.SRV_ADD_NAMES_DB) {
				addGender("заполняем таблицу мужских имен")
			}

			finishOperation()
		}.build()
	}
}