package ru.md.msc.domain.medal.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.base_domain.client.MicroClient
import ru.md.cor.ICorChainDsl
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.msc.domain.base.validate.auth.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.auth.validateAuthDeptLevel
import ru.md.msc.domain.base.validate.auth.validateAuthDeptTopLevelForView
import ru.md.base_domain.biz.validate.validateImageId
import ru.md.msc.domain.base.workers.chain.deleteS3ImageOnFailingChain
import ru.md.msc.domain.base.workers.chain.validateDeptIdAndAdminDeptLevelChain
import ru.md.msc.domain.base.workers.image.addImageToS3
import ru.md.msc.domain.base.workers.image.deleteBaseImageFromS3
import ru.md.msc.domain.base.workers.image.deleteBaseImagesFromS3
import ru.md.msc.domain.base.workers.image.getGalleryItemByClient
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.medal.biz.validate.validateMedalId
import ru.md.msc.domain.medal.biz.validate.validateMedalName
import ru.md.msc.domain.medal.biz.validate.validateMedalScore
import ru.md.msc.domain.medal.biz.workers.*
import ru.md.msc.domain.medal.service.MedalService
import ru.md.msc.domain.msg.service.MessageService
import ru.md.msc.domain.s3.repository.S3Repository
import ru.md.msc.domain.user.service.UserService

@Component
class MedalProcessor(
	private val userService: UserService,
	private val deptService: DeptService,
	private val medalService: MedalService,
	private val s3Repository: S3Repository,
	private val microClient: MicroClient,
	private val messageService: MessageService,
) : IBaseProcessor<MedalContext> {

	override suspend fun exec(ctx: MedalContext) = businessChain.exec(ctx.also {
		it.userService = userService
		it.deptService = deptService
		it.medalService = medalService
		it.s3Repository = s3Repository
		it.microClient = microClient
		it.messageService = messageService
	})

	companion object {

		private val businessChain = rootChain<MedalContext> {
			initStatus()

			operation("Создать медаль", MedalCommand.CREATE) {
				validateMainMedalFieldChain()
				worker("Для проверки") { deptId = medal.dept?.id ?: 0}
				validateDeptIdAndAdminDeptLevelChain()
				trimFieldMedalDetails("Очищаем поля")
				createMedal("Создаем медаль")
			}

			operation("Обновить награду", MedalCommand.UPDATE) {
				validateMainMedalFieldChain()
				validateMedalIdAndAccessToMedalChain()
				trimFieldMedalDetails("Очищаем поля")
				updateMedal("Обновляем медаль")
			}

			operation("Получить по id", MedalCommand.GET_BY_ID) {
				validateMedalIdAndViewAccessToMedalChain()
				getMedalByIdDetails("Получаем медаль с детализацией")
			}

			operation("Удалить медаль", MedalCommand.DELETE) {
				validateMedalIdAndAccessToMedalChain()
				getMedalByIdDetails("Получаем детальную награду")
				deleteMedal("Удаляем")
				worker("Подготовка к удалению изображений") { baseImages = medalDetails.images }
				deleteBaseImagesFromS3("Удаляем все изображения")
			}

			operation("Добавление изображения", MedalCommand.IMG_ADD) {
				worker("Получение id сущности") { medalId = fileData.entityId }
				validateMedalIdAndAccessToMedalChain()
				prepareMedalImagePrefixUrl("Получаем префикс изображения")
				addImageToS3("Сохраняем изображение в s3")
				addMedalImageToDb("Сохраняем ссылки на изображение в БД")
				updateMedalMainImage("Обновление основного изображения")
				deleteS3ImageOnFailingChain()
				getMedalByIdDetails("Получаем медаль с детализацией")
			}

			operation("Добавление изображения из галереи", MedalCommand.IMG_ADD_GALLERY) {
				validateImageId("Проверка imageId")
				validateMedalIdAndAccessToMedalChain()
				getGalleryItemByClient("Получаем объект галереи из мс")
				addMedalGalleryImageToDb("Сохраняем ссылки на изображение в БД")
				updateMedalMainImage("Обновление основного изображения")
			}

			operation("Удаление изображения", MedalCommand.IMG_DELETE) {
				validateImageId("Проверка imageId")
				validateMedalIdAndAccessToMedalChain()
				deleteMedalImageFromDb("Удаляем изображение из БД")
				deleteBaseImageFromS3("Удаляем изображение из s3")
				updateMedalMainImage("Обновление основного изображения")
			}

//			operation("Получить награды в отделе или подотделах", AwardCommand.GET_BY_DEPT) {
//				validateDeptId("Проверяем deptId")
//				validatePageParamsChain()
////				worker("Допустимые поля сортировки") { orderFields = listOf("name", "type", "startDate", "endDate") }
//				setAwardWithDeptValidSortedFields("Устанавливаем допустимые поля сортировки")
//				validateSortedFields("Проверка списка полей сортировки")
//				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
//				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
//				getAwardsByDept("Получаем награды из отдела или всех подотделов")
//			}
//
//
//			operation("Добавить действие в активность награждения", AwardCommand.ADD_ACTION) {
//				validateUserId("Проверка userId")
//				validateAwardId("Проверяем awardId")
//				validateActionType("Проверяем тип действия")
//				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
//				getAwardById("Получаем награду")
//				validateAwardPeriod("Проверяем период период действия награды")
//				worker("Получаем deptId для авторизации") { deptId = award.dept.id }
//				validateAuthDeptLevel("Проверка доступа к отделу награды")
//				validateAwardToUserAccess("Проверка доступности (по дереву отделов) награждения сотрудника этой наградой")
//				addAwardAction("Добавляем операцию в активность")
//				prepareSendActionMessageToUser("Подготовка к отправке сообщения")
//				sendMessage("Отправляем сообщение")
//			}
//
//			operation("Получить активные награды сотрудника", AwardCommand.GET_ACTIVE_AWARD_BY_USER) {
//				validateUserId("Проверка userId")
//				validatePageParamsChain()
//				setActionByUserValidSortedFields("Устанавливаем допустимые поля сортировки")
//				validateSortedFields("Проверка списка полей сортировки")
//				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
//				validateAuthUserTopLevelForView("Проверка доступа к данным сотрудника для просмотра")
//				getActiveAwardsByUser("Получаем награды сотрудника")
//			}
//
//			operation("Получить активные награды в отделе", AwardCommand.GET_ACTIVE_AWARD_BY_DEPT) {
//				validateDeptId("Проверка deptId")
//				validatePageParamsChain()
//				setActionByDeptValidSortedFields("Устанавливаем допустимые поля сортировки")
//				validateSortedFields("Проверка списка полей сортировки")
//				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
//				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
//				getActiveAwardsByDept("Получаем награды в отделе")
//			}
//
//			operation("Получить сотрудников, награжденных наградой", AwardCommand.GET_USERS_BY_ACTIVE_AWARD) {
//				validatePageParamsChain()
//				setActionByAwardValidSortedFields("Устанавливаем допустимые поля сортировки")
//				validateSortedFields("Проверка списка полей сортировки")
//				validateViewAccessToAwardChain()
//				getUsersByActiveAward("Получаем награды сотрудника")
//			}
//
//			operation("Получить доступные для награждения медали", AwardCommand.GET_ADMIN_AVAILABLE) {
//				validatePageParamsChain()
//				setAwardWithDeptValidSortedFields("Устанавливаем допустимые поля сортировки")
//				validateSortedFields("Проверка списка полей сортировки")
//				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
//				getAvailableAwardsBySubdepts("Получаем доступные награды")
//			}
//
//			operation(
//				"Получить доступные для награждения медали, кроме уже полученных сотрудником",
//				AwardCommand.GET_ADMIN_AVAILABLE_USER_EXCLUDE
//			) {
//				validateUserId("Проверяем userId")
//				validatePageParamsChain()
//				setAwardWithDeptValidSortedFields("Устанавливаем допустимые поля сортировки")
//				validateSortedFields("Проверка списка полей сортировки")
//				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
//				getAvailableAwardsUserExclude("Получаем доступные награды, кроме тех, которыми награжден сотрудник")
//			}
//
//			operation(
//				"Получить доступные для награждения простые медали, кроме уже полученных сотрудником",
//				AwardCommand.GET_SIMPLE_AWARD_AVAILABLE_USER_EXCLUDE
//			) {
//				validateUserId("Проверяем userId")
//				validatePageParamsChain()
//				setAwardWithDeptValidSortedFields("Устанавливаем допустимые поля сортировки")
//				validateSortedFields("Проверка списка полей сортировки")
//				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
//				getSimpleAvailableAwardsUserExclude("Получаем доступные простые награды")
//			}
//
//			operation("Количество наград в отделе (включая подотделы - опц.)", AwardCommand.COUNT_BY_DEPTS) {
//				validateDeptId("Проверяем deptId")
//				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
//				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
//				getAwardCountByDepts("Получаем количество наград")
//			}
//
//			operation("Количество активных награждений (включая ближние/дальние подотделы)", AwardCommand.COUNT_ACTIV) {
//				validateDeptId("Проверяем deptId")
//				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
//				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
//				getActivCountByDepts("Получаем количество награждений")
//			}
//
//			operation("Количество активных награждений от корневого отдела", AwardCommand.COUNT_ACTIV_ROOT) {
//				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
////				validateAuthDeptLevel("Проверка доступа к отделу")
//				worker("Подготовка") { deptId = authUser.dept?.id ?: 0 }
//				validateDeptId("Проверяем deptId")
//				getRootDeptId("Получаем корневой отдел")
//				worker("Подготовка") { deptId = rootDeptId }
//				getActivCountByDepts("Получаем количество награждений")
//			}
//
//			operation("Количество сотрудников с наградами и без", AwardCommand.COUNT_USER_AWARD_WW) {
//				validateDeptId("Проверяем deptId")
//				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
//				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
//				getUserAwardWWCountByDepts("Получаем количество сотрудников с наградами и без")
//			}
//
//			operation("Установить главные изображения у всех", AwardCommand.SET_MAIN_IMG) {
//				setMainImagesForAwards("Устанавливаем главные изображения")
//			}

			finishOperation()
		}.build()

		private fun ICorChainDsl<MedalContext>.validateMainMedalFieldChain() {
			validateMedalName("Проверяем наименование")
			validateMedalScore("Проверяем цену медали")
		}

		private fun ICorChainDsl<MedalContext>.validateMedalIdAndViewAccessToMedalChain() {
			validateMedalId("Проверяем awardId")
			getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
			getDeptIdByMedalId("Получаем deptId")
			validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
		}

		private fun ICorChainDsl<MedalContext>.validateMedalIdAndAccessToMedalChain() {
			validateMedalId("Проверяем awardId")
			getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
			getDeptIdByMedalId("Получаем deptId")
			validateAuthDeptLevel("Проверка доступа к отделу")
		}
	}
}