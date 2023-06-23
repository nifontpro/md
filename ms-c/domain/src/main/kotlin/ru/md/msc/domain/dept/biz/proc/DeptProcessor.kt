package ru.md.msc.domain.dept.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.validate.validateSortedFields
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.msc.domain.base.validate.db.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.db.validateAuthDeptLevel
import ru.md.msc.domain.base.validate.validateDeptId
import ru.md.msc.domain.base.validate.validateImageId
import ru.md.msc.domain.base.workers.chain.deleteS3ImageOnFailingChain
import ru.md.msc.domain.base.workers.chain.validateAdminDeptLevelChain
import ru.md.msc.domain.base.workers.image.deleteBaseImageFromS3
import ru.md.msc.domain.base.workers.image.deleteBaseImagesFromS3
import ru.md.msc.domain.base.workers.image.addImageToS3
import ru.md.msc.domain.dept.biz.validate.validateDeptName
import ru.md.msc.domain.dept.biz.workers.*
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.s3.repository.S3Repository
import ru.md.msc.domain.user.service.UserService

@Component
class DeptProcessor(
	private val userService: UserService,
	private val deptService: DeptService,
	private val s3Repository: S3Repository
) : IBaseProcessor<DeptContext> {

	override suspend fun exec(ctx: DeptContext) = businessChain.exec(ctx.also {
		it.userService = userService
		it.deptService = deptService
		it.s3Repository = s3Repository
	})

	companion object {

		private val businessChain = rootChain<DeptContext> {
			initStatus()

			operation("Создать отдел", DeptCommand.CREATE) {
				validateDeptName("Проверяем имя отдела")
				worker("Для проверки доступа к какому отделу") { deptId = dept.parentId }
				validateDeptId("Проверка deptId")
				validateAdminDeptLevelChain()
				trimFieldDeptDetails("Очищаем поля")
				createDept("Создаем отдел")
				createTestUsers("Создаем тестовых сотрудников")
			}

			operation("Получить поддерево отделов", DeptCommand.GET_AUTH_SUB_TREE) {
				worker("Допустимые поля сортировки") { orderFields = listOf("parentId", "name", "classname") }
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getSubtreeDepts("Получаем поддерево отделов")
			}

			operation("Получить поддерево отделов верхнего уровня доступа", DeptCommand.GET_TOP_LEVEL_TREE) {
				worker("Допустимые поля сортировки") { orderFields = listOf("parentId", "name", "classname") }
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getTopLevelTreeDepts("Получаем поддерево отделов верхнего уровня")
			}

			operation("Получить отдел по id", DeptCommand.GET_DEPT_BY_ID_DETAILS) {
				validateDeptId("Проверяем deptId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptLevel("Проверка доступа к отделу")
				getDeptDetailsById("Получаем отдел")
			}

			operation("Обновить профиль", DeptCommand.UPDATE) {
				validateDeptName("Проверяем имя отдела")
				validateAdminDeptLevelChain()
				trimFieldDeptDetails("Очищаем поля")
				updateDept("Обновляем профиль")
			}

			operation("Удалить отдел", DeptCommand.DELETE) {
				validateAdminDeptLevelChain()
				getDeptDetailsById("Получаем отдел")
				deleteDept("Удаляем отдел")
				worker("Подготовка к удалению изображений") { baseImages = deptDetails.dept.images }
				deleteBaseImagesFromS3("Удаляем все изображения")
			}

			operation("Добавление изображения", DeptCommand.IMG_ADD) {
				worker("Получение id сущности") { deptId = fileData.entityId }
				validateAdminDeptLevelChain()
				prepareDeptImagePrefixUrl("Получаем префикс изображения")
				addImageToS3("Сохраняем изображение в s3")
				addDeptImageToDb("Добавляем картинку в БД")
				updateDeptMainImage("Обновление основного изображения")
				deleteS3ImageOnFailingChain()
			}

			operation("Удаление изображения", DeptCommand.IMG_DELETE) {
				validateImageId("Проверка imageId")
				validateAdminDeptLevelChain()
				deleteDeptImageFromDb("Удаляем изображение")
				deleteBaseImageFromS3("Удаляем изображение из s3")
				updateDeptMainImage("Обновление основного изображения")
			}

			operation("Установить главные изображения у всех", DeptCommand.SET_MAIN_IMG) {
				setMainImagesForDepts("Устанавливаем главные изображения")
			}

			finishOperation()
		}.build()

	}
}