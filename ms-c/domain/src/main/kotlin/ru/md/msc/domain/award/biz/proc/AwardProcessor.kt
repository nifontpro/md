package ru.md.msc.domain.award.biz.proc

import org.springframework.stereotype.Component
import ru.md.cor.rootChain
import ru.md.cor.worker
import ru.md.msc.domain.award.biz.validate.validateAwardDates
import ru.md.msc.domain.award.biz.validate.validateAwardName
import ru.md.msc.domain.award.biz.validate.validateAwardType
import ru.md.msc.domain.award.biz.workers.createAward
import ru.md.msc.domain.award.biz.workers.trimFieldAwardDetails
import ru.md.msc.domain.award.service.AwardService
import ru.md.msc.domain.base.biz.IBaseProcessor
import ru.md.msc.domain.base.validate.db.getAuthUserAndVerifyEmail
import ru.md.msc.domain.base.validate.db.validateAuthDeptLevel
import ru.md.msc.domain.base.validate.validateDeptId
import ru.md.msc.domain.base.validate.validateImageId
import ru.md.msc.domain.base.workers.chain.validateAdminDeptLevel
import ru.md.msc.domain.base.workers.finishOperation
import ru.md.msc.domain.base.workers.initStatus
import ru.md.msc.domain.base.workers.operation
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.user.service.UserService

@Component
class AwardProcessor(
	private val userService: UserService,
	private val deptService: DeptService,
	private val awardService: AwardService,
) : IBaseProcessor<AwardContext> {

	override suspend fun exec(ctx: AwardContext) = businessChain.exec(ctx.also {
		it.userService = userService
		it.deptService = deptService
		it.awardService = awardService
	})

	companion object {

		private val businessChain = rootChain<AwardContext> {
			initStatus()

			operation("Создать награду", AwardCommand.CREATE) {
				validateAwardName("Проверяем имя")
				validateAwardType("Проверяем тип")
				validateAwardDates("Проверяем даты")
				validateAdminDeptLevel()
				trimFieldAwardDetails("Очищаем поля")
				createAward("Создаем награду")
			}

			operation("Получить отдел по id", AwardCommand.GET_BY_ID_DETAILS) {
				validateDeptId("Проверяем deptId")
				getAuthUserAndVerifyEmail("Проверка авторизованного пользователя по authId")
				validateAuthDeptLevel("Проверка доступа к отделу")
//				getDeptDetailsById("Получаем отдел")
			}

			operation("Обновить профиль", AwardCommand.UPDATE) {

			}

			operation("Удалить отдел", AwardCommand.DELETE) {

			}

			operation("Добавление изображения", AwardCommand.IMG_ADD) {
				worker("Получение id сущности") { deptId = fileData.entityId }
				validateAdminDeptLevel()
//				addDeptImage("Добавляем картинку")
			}

			operation("Обновление изображения", AwardCommand.IMG_UPDATE) {
				validateImageId("Проверка imageId")
				worker("Получение id сущности") { deptId = fileData.entityId }
				validateAdminDeptLevel()
//				updateDeptImage("Обновляем картинку")
			}

			finishOperation()
		}.build()

	}
}