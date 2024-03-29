package ru.md.msc.domain.award.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.validate.*
import ru.md.base_domain.biz.validate.chain.validateDeptIdAndAdminDeptLevelChain
import ru.md.base_domain.biz.validate.chain.validatePageParamsChain
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.base_domain.client.MicroClient
import ru.md.base_domain.dept.biz.validate.validateDeptId
import ru.md.base_domain.dept.service.BaseDeptService
import ru.md.base_domain.image.biz.chain.deleteS3ImageOnFailingChain
import ru.md.base_domain.image.biz.validate.validateImageId
import ru.md.base_domain.image.biz.workers.addImageToS3
import ru.md.base_domain.image.biz.workers.deleteAllBaseImagesFromS3
import ru.md.base_domain.image.biz.workers.deleteBaseImageFromS3
import ru.md.base_domain.s3.repo.BaseS3Repository
import ru.md.base_domain.user.biz.validate.validateUserId
import ru.md.base_domain.user.biz.workers.getAuthUserAndVerifyEmail
import ru.md.base_domain.user.biz.workers.getUserById
import ru.md.base_domain.user.service.BaseUserService
import ru.md.cor.ICorChainDsl
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.validate.*
import ru.md.msc.domain.award.biz.workers.*
import ru.md.msc.domain.award.biz.workers.message.prepareSendActionMessageToUser
import ru.md.msc.domain.award.biz.workers.sort.setActionByAwardValidSortedFields
import ru.md.msc.domain.award.biz.workers.sort.setActionByDeptValidSortedFields
import ru.md.msc.domain.award.biz.workers.sort.setActionByUserValidSortedFields
import ru.md.msc.domain.award.biz.workers.sort.setAwardWithDeptValidSortedFields
import ru.md.msc.domain.award.service.AwardService
import ru.md.msc.domain.base.workers.image.getGalleryItemByClient
import ru.md.msc.domain.base.workers.image.getGalleryItemTest
import ru.md.msc.domain.base.workers.msg.sendMessage
import ru.md.msc.domain.base.workers.msg.sendMessageToEmail
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.email.EmailService
import ru.md.msc.domain.msg.service.MessageService
import ru.md.msc.domain.user.service.UserService

@Component
class AwardProcessor(
	private val baseDeptService: BaseDeptService,
	private val baseUserService: BaseUserService,
	private val userService: UserService,
	private val deptService: DeptService,
	private val awardService: AwardService,
	private val baseS3Repository: BaseS3Repository,
	private val microClient: MicroClient,
	private val messageService: MessageService,
	private val emailService: EmailService,
) : IBaseProcessor<AwardContext> {

	override suspend fun exec(ctx: AwardContext) = businessChain.exec(ctx.also {
		it.baseDeptService = baseDeptService
		it.baseUserService = baseUserService
		it.userService = userService
		it.deptService = deptService
		it.awardService = awardService
		it.baseS3Repository = baseS3Repository
		it.microClient = microClient
		it.messageService = messageService
		it.emailService = emailService
	})

	companion object {

		private val businessChain = rootChain<AwardContext> {
			initStatus()

			operation("Создать награду", AwardCommand.CREATE) {
				validateMainAwardFieldChain()
				validateDeptIdAndAdminDeptLevelChain()
				trimFieldAwardDetails("Очищаем поля")
				createAward("Создаем награду")
			}

			operation("Обновить награду", AwardCommand.UPDATE) {
				validateMainAwardFieldChain()
				validateAwardIdAndAccessToAwardChain()
				trimFieldAwardDetails("Очищаем поля")
				updateAward("Обновляем награду")
			}

			operation("Получить по id", AwardCommand.GET_BY_ID_DETAILS) {
				validateAwardIdAndViewAccessToAwardChain()
				getAwardByIdDetails("Получаем детальную награду")
			}

			operation("Получить награды в отделе или подотделах", AwardCommand.GET_BY_DEPT) {
				validateDeptId("Проверяем deptId")
				validatePageParamsChain()
				setAwardWithDeptValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getAwardsByDept("Получаем награды из отдела или всех подотделов")
			}

			operation("Удалить награду", AwardCommand.DELETE) {
				validateAwardIdAndAccessToAwardChain()
				getAwardByIdDetails("Получаем детальную награду")
				deleteAward("Удаляем")
				worker("Подготовка к удалению изображений") { baseImages = awardDetails.images }
				deleteAllBaseImagesFromS3("Удаляем все изображения")
			}

			operation("Добавление изображения", AwardCommand.IMG_ADD) {
				worker("Получение id сущности") { awardId = imageData.entityId }
				validateAccessAndDeleteOldImages()
				prepareAwardImagePrefixUrl("Получаем префикс изображения")
				addImageToS3("Сохраняем изображение в s3")
				deleteOldAndAddAwardImageToDb("Сохраняем ссылки на изображение в БД")
				updateAwardMainImage("Обновление основного изображения")
				deleteS3ImageOnFailingChain()
			}

			operation("Добавление изображения из галереи", AwardCommand.IMG_ADD_GALLERY) {
				validateAwardId("Проверяем awardId")
				validateImageId("Проверка imageId")
				validateAccessAndDeleteOldImages()
				getGalleryItemByClient("Получаем объект галереи из мс")
				addAwardGalleryImageToDb("Сохраняем ссылки на изображение в БД")
				updateAwardMainImage("Обновление основного изображения")
			}

			operation("Добавление изображения из галереи", AwardCommand.GET_GALLERY) {
				getGalleryItemTest("Получаем объект галереи из мс")
			}

			operation("Удаление изображения", AwardCommand.IMG_DELETE) {
				validateImageId("Проверка imageId")
				validateAwardIdAndAccessToAwardChain()
				deleteAwardImageFromDb("Удаляем изображение из БД")
				deleteBaseImageFromS3("Удаляем изображение из s3")
				updateAwardMainImage("Обновление основного изображения")
			}

			operation("Добавить действие в активность награждения", AwardCommand.ADD_ACTION) {
				validateUserId("Проверка userId")
				validateAwardId("Проверяем awardId")
				validateActionType("Проверяем тип действия")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getAwardById("Получаем награду")
				validateAwardPeriod("Проверяем период период действия награды")
				worker("Получаем deptId для авторизации") { deptId = award.dept.id }
				validateAuthDeptLevel("Проверка доступа к отделу награды")
				getUserById("Получаем профиль сотрудника")
//				validateAwardToUserAccess("Проверка доступности (по дереву отделов) награждения сотрудника этой наградой")
				addAwardAction("Добавляем операцию в активность")
				prepareSendActionMessageToUser("Подготовка к отправке сообщения")
				sendMessage("Отправляем сообщение")
				sendMessageToEmail("Отправляем сообщение на email")
			}

			operation("Получить активные награды сотрудника", AwardCommand.GET_ACTIVE_AWARD_BY_USER) {
				validateUserId("Проверка userId")
				validatePageParamsChain()
				setActionByUserValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthUserTopLevelForView("Проверка доступа к данным сотрудника для просмотра")
				getActiveAwardsByUser("Получаем награды сотрудника")
			}

			operation("Получить активные награды в отделе", AwardCommand.GET_ACTIVE_AWARD_BY_DEPT) {
				validateDeptId("Проверка deptId")
				validatePageParamsChain()
				setActionByDeptValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getActiveAwardsByDept("Получаем награды в отделе")
			}

			operation("Получить сотрудников, награжденных наградой", AwardCommand.GET_USERS_BY_ACTIVE_AWARD) {
				validatePageParamsChain()
				setActionByAwardValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				validateAwardIdAndViewAccessToAwardChain()
				getUsersByActiveAward("Получаем награды сотрудника")
			}

			operation("Получить доступные для награждения медали", AwardCommand.GET_ADMIN_AVAILABLE) {
				validatePageParamsChain()
				setAwardWithDeptValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getAvailableAwardsBySubdepts("Получаем доступные награды")
			}

			operation(
				"Получить доступные для награждения медали, кроме уже полученных сотрудником",
				AwardCommand.GET_ADMIN_AVAILABLE_USER_EXCLUDE
			) {
				validateUserId("Проверяем userId")
				validatePageParamsChain()
				setAwardWithDeptValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getAvailableAwardsUserExclude("Получаем доступные награды, кроме тех, которыми награжден сотрудник")
			}

			operation(
				"Получить доступные для награждения простые медали, кроме уже полученных сотрудником",
				AwardCommand.GET_SIMPLE_AWARD_AVAILABLE_USER_EXCLUDE
			) {
				validateUserId("Проверяем userId")
				validatePageParamsChain()
				setAwardWithDeptValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getSimpleAvailableAwardsUserExclude("Получаем доступные простые награды")
			}

			operation("Количество наград в отделе (включая подотделы - опц.)", AwardCommand.COUNT_BY_DEPTS) {
				validateDeptId("Проверяем deptId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getAwardCountByDepts("Получаем количество наград")
			}

			operation("Количество активных награждений (включая ближние/дальние подотделы)", AwardCommand.COUNT_ACTIV) {
				validateDeptId("Проверяем deptId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getActivCountByDepts("Получаем количество награждений")
			}

			operation("Количество активных награждений от корневого отдела", AwardCommand.COUNT_ACTIV_ROOT) {
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
//				validateAuthDeptLevel("Проверка доступа к отделу")
				worker("Подготовка") { deptId = authUser.dept?.id ?: 0 }
				validateDeptId("Проверяем deptId")
				getRootDeptId("Получаем корневой отдел")
				worker("Подготовка") { deptId = rootDeptId }
				getActivCountByDepts("Получаем количество награждений")
			}

			operation("Количество сотрудников с наградами и без", AwardCommand.COUNT_USER_AWARD_WW) {
				validateDeptId("Проверяем deptId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getUserAwardWWCountByDepts("Получаем количество сотрудников с наградами и без")
			}

			operation("Установить главные изображения у всех", AwardCommand.SET_MAIN_IMG) {
				setMainImagesForAwards("Устанавливаем главные изображения")
			}

			finishOperation()
		}.build()

		private fun ICorChainDsl<AwardContext>.validateAccessAndDeleteOldImages() {
			validateAwardIdAndAccessToAwardChain()
			// Удаляем старые изображения
			getAwardByIdDetails("Получаем детальную награду")
			worker("Подготовка к удалению изображений") { baseImages = awardDetails.images }
			deleteAllBaseImagesFromS3("Удаляем все изображения")
		}

		private fun ICorChainDsl<AwardContext>.validateMainAwardFieldChain() {
			validateAwardName("Проверяем имя")
			validateAwardType("Проверяем тип")
			validateAwardDates("Проверяем даты")
		}

		private fun ICorChainDsl<AwardContext>.validateAwardIdAndAccessToAwardChain() {
			validateAwardId("Проверяем awardId")
			getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
			validateAdminRole("Проверяем наличие прав Администратора")
			findDeptIdByAwardId("Получаем deptId")
			validateAuthDeptLevel("Проверка доступа к отделу")
		}

		private fun ICorChainDsl<AwardContext>.validateAwardIdAndViewAccessToAwardChain() {
			validateAwardId("Проверяем awardId")
//			getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
//			findDeptIdByAwardId("Получаем deptId")
//			validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
		}

	}
}