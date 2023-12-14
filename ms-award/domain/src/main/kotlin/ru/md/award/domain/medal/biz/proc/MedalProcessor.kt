package ru.md.award.domain.medal.biz.proc

import org.springframework.stereotype.Component
import ru.md.award.domain.medal.biz.validate.validateMedalId
import ru.md.award.domain.medal.biz.validate.validateMedalName
import ru.md.award.domain.medal.biz.validate.validateMedalScore
import ru.md.award.domain.medal.biz.workers.*
import ru.md.award.domain.medal.service.MedalImageService
import ru.md.award.domain.medal.service.MedalService
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.validate.chain.validateDeptIdAndAdminDeptLevelChain
import ru.md.base_domain.biz.validate.validateAuthDeptLevel
import ru.md.base_domain.biz.validate.validateAuthDeptTopLevelForView
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.base_domain.client.MicroClient
import ru.md.base_domain.dept.service.BaseDeptService
import ru.md.base_domain.image.biz.chain.deleteS3ImageOnFailingChain
import ru.md.base_domain.image.biz.validate.validateImageId
import ru.md.base_domain.image.biz.workers.addImageToS3Mem
import ru.md.base_domain.image.biz.workers.deleteAllBaseImagesFromS3
import ru.md.base_domain.image.biz.workers.deleteBaseImageFromS3
import ru.md.base_domain.s3.repo.BaseS3Repository
import ru.md.base_domain.user.biz.workers.getAuthUserAndVerifyEmail
import ru.md.base_domain.user.service.BaseUserService
import ru.md.cor.ICorChainDsl
import ru.md.cor.rootChain
import ru.md.cor.worker

@Component
class MedalProcessor(
	private val baseDeptService: BaseDeptService,
	private val baseUserService: BaseUserService,
	private val medalService: MedalService,
	private val medalImageService: MedalImageService,
	private val baseS3Repository: BaseS3Repository,
	private val microClient: MicroClient,
//	private val messageService: MessageService,
) : IBaseProcessor<MedalContext> {

	override suspend fun exec(ctx: MedalContext) = businessChain.exec(ctx.also {
		it.baseDeptService = baseDeptService
		it.baseUserService = baseUserService
		it.medalService = medalService
		it.medalImageService = medalImageService
		it.baseS3Repository = baseS3Repository
		it.microClient = microClient
//		it.messageService = messageService
	})

	companion object {

		private val businessChain = rootChain<MedalContext> {
			initStatus()

			operation("Создать медаль", MedalCommand.CREATE) {
				validateMainMedalFieldChain()
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
				deleteAllBaseImagesFromS3("Удаляем все изображения")
			}

			operation("Добавление изображения", MedalCommand.IMG_ADD) {
				worker("Получение id сущности") { medalId = imageData.entityId }
				validateMedalIdAndAccessToMedalChain()
				prepareMedalImagePrefixUrl("Получаем префикс изображения")
				addImageToS3Mem("Сохраняем изображение в s3")
				addMedalImageToDb("Сохраняем ссылки на изображение в БД")
				updateMedalMainImage("Обновление основного изображения")
				deleteS3ImageOnFailingChain()
				getMedalByIdDetails("Получаем медаль с детализацией")
			}

			operation("Добавление изображения из галереи", MedalCommand.IMG_ADD_GALLERY) {
				validateImageId("Проверка imageId")
				validateMedalIdAndAccessToMedalChain()
//				getGalleryItemByClient("Получаем объект галереи из мс")
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