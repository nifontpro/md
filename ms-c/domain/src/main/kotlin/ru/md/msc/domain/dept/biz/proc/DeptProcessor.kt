package ru.md.msc.domain.dept.biz.proc

import org.springframework.stereotype.Component
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.msc.domain.base.biz.IBaseProcessor
import ru.md.msc.domain.base.validate.db.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.db.validateAuthDeptLevel
import ru.md.msc.domain.base.validate.validateDeptId
import ru.md.msc.domain.base.validate.validateImageId
import ru.md.msc.domain.base.validate.validateSortedFields
import ru.md.msc.domain.base.workers.chain.validateAdminDeptLevelChain
import ru.md.msc.domain.base.workers.finishOperation
import ru.md.msc.domain.base.workers.initStatus
import ru.md.msc.domain.base.workers.operation
import ru.md.msc.domain.dept.biz.validate.validateDeptName
import ru.md.msc.domain.dept.biz.workers.*
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.user.service.UserService

@Component
class DeptProcessor(
	private val userService: UserService,
	private val deptService: DeptService
) : IBaseProcessor<DeptContext> {

	override suspend fun exec(ctx: DeptContext) = businessChain.exec(ctx.also {
		it.userService = userService
		it.deptService = deptService
	})

	companion object {

		private val businessChain = rootChain<DeptContext> {
			initStatus()

			operation("Создать отдел", DeptCommand.CREATE) {
				validateDeptName("Проверяем имя отдела")
				worker("Для проверки доступа к какому отделу") { deptId = dept.parentId }
				validateAdminDeptLevelChain()
				trimFieldDeptDetails("Очищаем поля")
				createDept("Создаем отдел")
				createTestUsers("Создаем тестовых сотрудников")
			}

			operation("Получить поддерево отделов", DeptCommand.GET_DEPTS_TREE) {
				worker("Допустимые поля сортировки") { orderFields = listOf("parentId", "name", "classname") }
				validateSortedFields("Проверка списка сортировки")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				getSubtreeDepts("Получаем поддерево отделов")
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
			}

			operation("Добавление изображения", DeptCommand.IMG_ADD) {
				worker("Получение id сущности") { deptId = fileData.entityId }
				validateAdminDeptLevelChain()
				addDeptImage("Добавляем картинку")
			}

			operation("Обновление изображения", DeptCommand.IMG_UPDATE) {
				validateImageId("Проверка imageId")
				worker("Получение id сущности") { deptId = fileData.entityId }
				validateAdminDeptLevelChain()
				updateDeptImage("Обновляем картинку")
			}

			operation("Удаление изображения", DeptCommand.IMG_DELETE) {
				validateImageId("Проверка imageId")
				validateAdminDeptLevelChain()
				deleteDeptImage("Удаляем изображение")
			}

			finishOperation()
		}.build()

	}
}