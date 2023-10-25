package ru.md.msc.domain.dept.biz.proc

import org.springframework.stereotype.Component
import ru.md.base_domain.biz.proc.IBaseProcessor
import ru.md.base_domain.biz.validate.validateSortedFields
import ru.md.base_domain.biz.workers.finishOperation
import ru.md.base_domain.biz.workers.initStatus
import ru.md.base_domain.biz.workers.operation
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.msc.domain.base.validate.auth.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.auth.validateAuthDeptTopLevelForView
import ru.md.base_domain.biz.validate.validateDeptId
import ru.md.base_domain.biz.validate.validateImageId
import ru.md.msc.domain.base.workers.chain.deleteS3ImageOnFailingChain
import ru.md.msc.domain.base.workers.chain.validateDeptIdAndAdminDeptLevelChain
import ru.md.msc.domain.base.workers.image.addImageToS3
import ru.md.msc.domain.base.workers.image.deleteBaseImageFromS3
import ru.md.msc.domain.base.workers.image.deleteBaseImagesFromS3
import ru.md.msc.domain.dept.biz.validate.validateDeptName
import ru.md.msc.domain.dept.biz.validate.validateDeptNameExist
import ru.md.msc.domain.dept.biz.workers.*
import ru.md.msc.domain.dept.biz.workers.chain.getDeptListByParentDeptIdChain
import ru.md.msc.domain.dept.service.DeptService
import ru.md.base_domain.s3.repo.BaseS3Repository
import ru.md.msc.domain.user.service.UserService

@Component
class DeptProcessor(
	private val userService: UserService,
	private val deptService: DeptService,
	private val baseS3Repository: BaseS3Repository
) : IBaseProcessor<DeptContext> {

	override suspend fun exec(ctx: DeptContext) = businessChain.exec(ctx.also {
		it.userService = userService
		it.deptService = deptService
		it.baseS3Repository = baseS3Repository
	})

	companion object {

		private val businessChain = rootChain<DeptContext> {
			initStatus()

			operation("Создать отдел", DeptCommand.CREATE) {
				validateDeptName("Проверяем имя отдела")
				worker("Для проверки доступа к какому отделу") { deptId = dept.parentId }
				validateDeptIdAndAdminDeptLevelChain()
				trimFieldDeptDetails("Очищаем поля")
				validateDeptNameExist("Проверка существования отдела с таким названием на этом уровне")
				createDept("Создаем отдел")
//				createTestUsers("Создаем тестовых сотрудников")
			}

			operation("Получить поддерево отделов", DeptCommand.GET_AUTH_SUB_TREE) {
				worker("Допустимые поля сортировки") { orderFields = listOf("parentId", "name", "classname") }
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getSubtreeDepts("Получаем поддерево отделов")
			}

			operation("Получить поддерево отделов с верхнего уровня доступа", DeptCommand.GET_TOP_LEVEL_TREE) {
				worker("Допустимые поля сортировки") { orderFields = listOf("parentId", "name", "classname") }
				validateSortedFields("Проверка списка полей сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getTopLevelTreeDepts("Получаем поддерево отделов верхнего уровня")
			}

			operation("Получить список потомков отдела deptId", DeptCommand.GET_CURRENT_DEPTS) {
				getDeptListByParentDeptIdChain()
			}

			operation("Получить отдел по id", DeptCommand.GET_DEPT_BY_ID) {
				validateDeptId("Проверяем deptId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getDeptById("Получаем отдел")
			}


			operation("Получить отдел по id с деталями", DeptCommand.GET_DEPT_BY_ID_DETAILS) {
				validateDeptId("Проверяем deptId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptTopLevelForView("Проверка доступа к чтению данных отдела")
				getDeptDetailsById("Получаем отдел")
			}

			operation("Получить отдел авторизованного пользователя", DeptCommand.GET_AUTH_DEPT) {
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				worker("Подготовка") { deptId = authUser.dept?.id ?: 0 }
				getDeptById("Получаем отдел")
			}

			operation("Обновить профиль", DeptCommand.UPDATE) {
				validateDeptName("Проверяем имя отдела")
				validateDeptIdAndAdminDeptLevelChain()
				trimFieldDeptDetails("Очищаем поля")
				updateDept("Обновляем профиль")
			}

			operation("Удалить отдел", DeptCommand.DELETE) {
				validateDeptIdAndAdminDeptLevelChain()
				getDeptDetailsById("Получаем отдел")
				deleteDept("Удаляем отдел")
				worker("Подготовка к удалению изображений") { baseImages = deptDetails.dept.images }
				deleteBaseImagesFromS3("Удаляем все изображения")
			}

			operation("Добавление изображения", DeptCommand.IMG_ADD) {
				worker("Получение id сущности") { deptId = fileData.entityId }
				validateDeptIdAndAdminDeptLevelChain()
				prepareDeptImagePrefixUrl("Получаем префикс изображения")
				addImageToS3("Сохраняем изображение в s3")
				addDeptImageToDb("Добавляем картинку в БД")
				updateDeptMainImage("Обновление основного изображения")
				deleteS3ImageOnFailingChain()
			}

			operation("Удаление изображения", DeptCommand.IMG_DELETE) {
				validateImageId("Проверка imageId")
				validateDeptIdAndAdminDeptLevelChain()
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