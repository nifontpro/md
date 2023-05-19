package ru.md.msc.domain.award.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.validate.validateSortedFields
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.base_domain.client.MicroClient
import ru.md.cor.ICorChainDsl
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.validate.*
import ru.md.msc.domain.award.biz.workers.*
import ru.md.msc.domain.award.biz.workers.sort.setActionByAwardValidSortedFields
import ru.md.msc.domain.award.biz.workers.sort.setActionByDeptValidSortedFields
import ru.md.msc.domain.award.biz.workers.sort.setActionByUserValidSortedFields
import ru.md.msc.domain.award.biz.workers.sort.setAwardWithDeptValidSortedFields
import ru.md.msc.domain.award.service.AwardService
import ru.md.msc.domain.base.validate.db.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.db.validateAuthDeptLevel
import ru.md.msc.domain.base.validate.validateDeptId
import ru.md.msc.domain.base.validate.validateImageId
import ru.md.msc.domain.base.validate.validateUserId
import ru.md.msc.domain.base.workers.chain.deleteS3ImageOnFailingChain
import ru.md.msc.domain.base.workers.chain.validateAdminDeptLevelChain
import ru.md.msc.domain.base.workers.chain.validatePageParamsChain
import ru.md.msc.domain.base.workers.deleteBaseImageFromS3
import ru.md.msc.domain.base.workers.deleteBaseImagesFromS3
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.s3.repository.S3Repository
import ru.md.msc.domain.user.service.UserService

@Component
class AwardProcessor(
	private val userService: UserService,
	private val deptService: DeptService,
	private val awardService: AwardService,
	private val s3Repository: S3Repository,
	private val microClient: MicroClient
) : IBaseProcessor<AwardContext> {

	override suspend fun exec(ctx: AwardContext) = businessChain.exec(ctx.also {
		it.userService = userService
		it.deptService = deptService
		it.awardService = awardService
		it.s3Repository = s3Repository
		it.microClient = microClient
	})

	companion object {

		private val businessChain = rootChain<AwardContext> {
			initStatus()

			operation("Создать награду", AwardCommand.CREATE) {
				validateMainAwardFieldChain()
				validateAdminDeptLevelChain()
				trimFieldAwardDetails("Очищаем поля")
				createAward("Создаем награду")
			}

			operation("Обновить награду", AwardCommand.UPDATE) {
				validateMainAwardFieldChain()
				validateAccessToAwardChain()
				trimFieldAwardDetails("Очищаем поля")
				updateAward("Обновляем награду")
			}

			operation("Получить по id", AwardCommand.GET_BY_ID_DETAILS) {
				validateAccessToAwardChain()
				getAwardByIdDetails("Получаем детальную награду")
			}

			operation("Получить награды в отделе", AwardCommand.GET_BY_DEPT) {
				validateDeptId("Проверяем deptId")
				validatePageParamsChain()
				worker("Допустимые поля сортировки") { orderFields = listOf("name", "type", "startDate", "endDate") }
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptLevel("Проверка доступа к отделу")
				getAwardsByDept("Получаем награды из отдела")
			}

			operation("Удалить награду", AwardCommand.DELETE) {
				validateAccessToAwardChain()
				getAwardByIdDetails("Получаем детальную награду")
				deleteAward("Удаляем")
				worker("Подготовка к удалению изображений") { baseImages = awardDetails.award.images }
				deleteBaseImagesFromS3("Удаляем все изображения")
			}

			operation("Добавление изображения", AwardCommand.IMG_ADD) {
				worker("Получение id сущности") { awardId = fileData.entityId }
				validateAccessToAwardChain()
				addAwardImageToS3("Сохраняем изображение в s3")
				addAwardImageToDb("Сохраняем ссылки на изображение в БД")
				deleteS3ImageOnFailingChain()
			}

			operation("Добавление изображения из галереи", AwardCommand.IMG_ADD_GALLERY) {
				validateAwardId("Проверяем awardId")
				validateImageId("Проверка imageId")
				validateAccessToAwardChain()
				getGalleryItemByClient("Получаем объект галереи из мс")
				addAwardGalleryImageToDb("Сохраняем ссылки на изображение в БД")
			}

			operation("Удаление изображения", AwardCommand.IMG_DELETE) {
				validateImageId("Проверка imageId")
				validateAccessToAwardChain()
				deleteAwardImageFromDb("Удаляем изображение из БД")
				deleteBaseImageFromS3("Удаляем изображение из s3")
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
				validateAwardToUserAccess("Проверка доступности (по дереву отделов) награждения сотрудника этой наградой")
				addAwardAction("Добавляем операцию в активность")
			}

			operation("Получить активные награды сотрудника", AwardCommand.GET_ACTIVE_AWARD_BY_USER) {
				validateUserId("Проверка userId")
				setActionByUserValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				// Проверка прав !!!
				getActiveAwardsByUser("Получаем награды сотрудника")
			}

			operation("Получить активные награды в отделе", AwardCommand.GET_ACTIVE_AWARD_BY_DEPT) {
				validateDeptId("Проверка deptId")
				validatePageParamsChain()
				setActionByDeptValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptLevel("Проверка доступа к отделу награды")
				getActiveAwardsByDept("Получаем награды в отделе")
			}

			operation("Получить сотрудников, награжденных наградой", AwardCommand.GET_USERS_BY_ACTIVE_AWARD) {
				setActionByAwardValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				validateAccessToAwardChain()
				getUsersByActiveAward("Получаем награды сотрудника")
			}

			operation("Получить доступные для награждения медали", AwardCommand.GET_ADMIN_AVAILABLE) {
				validatePageParamsChain()
				setAwardWithDeptValidSortedFields("Устанавливаем допустимые поля сортировки")
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getAvailableAwardsBySubdepts("Получаем доступные награды")
			}

			finishOperation()
		}.build()

		private fun ICorChainDsl<AwardContext>.validateMainAwardFieldChain() {
			validateAwardName("Проверяем имя")
			validateAwardType("Проверяем тип")
			validateAwardDates("Проверяем даты")
		}

		private fun ICorChainDsl<AwardContext>.validateAccessToAwardChain() {
			validateAwardId("Проверяем awardId")
			getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
			findDeptIdByAwardId("Получаем deptId")
			validateAuthDeptLevel("Проверка доступа к отделу")
		}

	}
}