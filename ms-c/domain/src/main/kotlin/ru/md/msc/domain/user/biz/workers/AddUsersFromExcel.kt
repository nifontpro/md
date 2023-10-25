package ru.md.msc.domain.user.biz.workers

import org.dhatim.fastexcel.reader.ReadableWorkbook
import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.helper.otherError
import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.base_domain.dept.model.Dept
import ru.md.msc.domain.user.biz.proc.*
import ru.md.msc.domain.user.biz.validate.*
import ru.md.base_domain.user.model.RoleUser
import ru.md.base_domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.domain.user.model.excel.AddUserReport
import java.io.FileInputStream

fun ICorChainDsl<UserContext>.addUsersFromExcel(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
		FileInputStream(fileUrl).use { file ->
			ReadableWorkbook(file).use { wb ->
				val sheet = wb.firstSheet
				for (row in sheet.read()) {
					row.getCellText(0).toLongOrNull() ?: continue
					val errors = mutableListOf<ContextError>()
					var success = true
					var userExist = false

					val lastname = row.getCellText(1).trim()
					if (lastname.isBlank()) {
						errors.add(validateUserLastnameBlankExt())
						success = false
					}

					val firstname = row.getCellText(2).trim()
					if (firstname.isBlank()) {
						errors.add(validateUserFirstnameBlankExt())
						success = false
					}

					val authEmail = row.getCellText(4).trim().lowercase()

					if (authEmail.isNotBlank()) {
						if (isValidEmail(authEmail)) {
							try {
								if (success) {
									userExist = userService.validateByDeptIdAndEmailExist(deptId = deptId, email = authEmail)
								}
							} catch (e: Exception) {
								log.error(e.message)
								success = false
								errors.add(getUserErrorExt())
							}
						} else {
							success = false
							errors.add(validateUserEmailFormatExt())
						}
					} else {
						success = false
						errors.add(validateUserEmailBlankExt())
					}

					user = User(
						lastname = lastname,
						firstname = firstname,
						patronymic = row.getCellText(3).trim(),
						authEmail = authEmail,
						post = row.getCellText(6).trim(),
						dept = Dept(id = deptId),
						roles = setOf(RoleUser.USER)
					)
					userDetails = UserDetails(
						user = user, phone = row.getCellText(5).trim(), description = row.getCellText(7).trim()
					)

					if (success) {
						if (userExist) {
							try {
								userDetails = userService.simpleUpdate(userDetails)
							} catch (e: UserNotFoundException) {
								log.error(e.message)
								errors.add(userNotFoundErrorExt())
							} catch (e: Exception) {
								log.error(e.message)
								errors.add(userUpdateErrorExt())
							}
						} else {
							try {
								userDetails = userService.create(userDetails)
							} catch (e: Exception) {
								log.error(e.message)
								errors.add(userCreateErrorExt())
							}
						}
					}

					addReport.add(
						AddUserReport(
							success = success, userDetails = userDetails, isUpdate = userExist, errors = errors
						)
					)

				}
			}
		}
	}

	except {
		log.error(it.message)
		fail(
			otherError(
				description = "Ошибка чтения Excel файла",
				field = "excel",
				code = "excel-read error",
				level = ContextError.Levels.ERROR
			)
		)
	}
}