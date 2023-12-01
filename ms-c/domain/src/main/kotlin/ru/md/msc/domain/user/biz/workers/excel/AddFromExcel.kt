package ru.md.msc.domain.user.biz.workers.excel

import org.dhatim.fastexcel.reader.ReadableWorkbook
import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.dept.biz.errors.DeptIOException
import ru.md.base_domain.dept.biz.errors.deptDbError
import ru.md.base_domain.dept.model.Dept
import ru.md.base_domain.dept.model.DeptType
import ru.md.base_domain.user.model.RoleUser
import ru.md.base_domain.user.model.User
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.event.biz.proc.EventIOException
import ru.md.msc.domain.event.model.UserEvent
import ru.md.msc.domain.user.biz.proc.*
import ru.md.msc.domain.user.biz.validate.isValidEmail
import ru.md.msc.domain.user.biz.workers.excel.ExcelFields.FIELD_BIRTH_DATE
import ru.md.msc.domain.user.biz.workers.excel.ExcelFields.FIELD_EMAIL
import ru.md.msc.domain.user.biz.workers.excel.ExcelFields.FIELD_FIO
import ru.md.msc.domain.user.biz.workers.excel.ExcelFields.FIELD_GENDER
import ru.md.msc.domain.user.biz.workers.excel.ExcelFields.FIELD_JOB_DATE
import ru.md.msc.domain.user.biz.workers.excel.ExcelFields.FIELD_PHONE
import ru.md.msc.domain.user.biz.workers.excel.ExcelFields.FIELD_POST
import ru.md.msc.domain.user.biz.workers.excel.ExcelFields.FIELD_ROLE
import ru.md.msc.domain.user.biz.workers.excel.ExcelFields.FIELD_SCHEDULE
import ru.md.msc.domain.user.biz.workers.excel.ExcelFields.FIELD_TAB_ID
import ru.md.msc.domain.user.model.FullName
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.domain.user.model.excel.AddUserReport
import ru.md.msc.domain.user.model.excel.LoadReport
import ru.md.msc.domain.user.model.excel.UpdateKey
import java.io.FileInputStream

fun ICorChainDsl<UserContext>.addFromExcel(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {

		FileInputStream(fileUrl).use { file ->
			ReadableWorkbook(file).use { wb ->
				val sheet = wb.firstSheet

				val rows = sheet.read()
				val maxRowIdx = rows.size - 1
				var currentRow = 0

				if (maxRowIdx < 2) {
					throw ParseExcelException("Слишком мало строк: $maxRowIdx")
				}

				/**
				 * Определение позиций основных полей
				 */
				var colFioNull: Int? = null
				var colTabId: Int? = null
				var colPost: Int? = null
				var colPhone: Int? = null
				var colSchedule: Int? = null
				var colEmail: Int? = null
				var colGender: Int? = null
				var colRole: Int? = null
				var colBirthData: Int? = null
				var colJobDate: Int? = null

				var isNumberFound = false
				run blockEach@{
					rows.forEachIndexed { idx, row ->
						currentRow = idx
						val ch = row.getCellText(0)
						if (ch == "№") {
							isNumberFound = true
							row.getCells(1, row.cellCount).forEach { cell ->
								val rowIdx = cell.columnIndex
								when (cell.text.trim().uppercase()) {
									FIELD_FIO.uppercase() -> colFioNull = rowIdx
									FIELD_TAB_ID.uppercase() -> colTabId = rowIdx
									FIELD_POST.uppercase() -> colPost = rowIdx
									FIELD_PHONE.uppercase() -> colPhone = rowIdx
									FIELD_SCHEDULE.uppercase() -> colSchedule = rowIdx
									FIELD_EMAIL.uppercase() -> colEmail = rowIdx
									FIELD_GENDER.uppercase() -> colGender = rowIdx
									FIELD_ROLE.uppercase() -> colRole = rowIdx
									FIELD_BIRTH_DATE.uppercase() -> colBirthData = rowIdx
									FIELD_JOB_DATE.uppercase() -> colJobDate = rowIdx
								}
							}
							return@blockEach
						}
					}
				}

				if (!isNumberFound) throw ParseExcelException("Не верный формат заголовка таблицы")
				if (colFioNull == null) throw ParseExcelException("Не определена позиция ФИО в таблице")
				val colFio = colFioNull ?: 0

				var currentDept: Dept? = null

				// Все отделы компании
				val deptsIds = baseDeptService.findSubTreeIds(deptId)

				val addReport: MutableList<AddUserReport> = mutableListOf()
				var createdDeptCount = 0
				var createdUserCount = 0
				var updatedUserCount = 0

				for (i in ++currentRow..maxRowIdx) {
					val userErrors = mutableListOf<ContextError>()
					val userEvents = mutableListOf<UserEvent>()
					var isUpdate = false

					val row = rows[i]
					val firstRowText = row.getCellText(0)
					if (firstRowText.isNullOrBlank()) continue  // Пустая строка пропускается

					if (firstRowText.isInt()) {
						/**
						 * Добавление/Обновление Сотрудника
						 */

						val fio = row.getCellText(colFio)
						val fioArr = fio.split(" ")

						val fullName = when (fioArr.size) {

							4 -> {
								FullName(
									lastName = fioArr[0].trim() + " " + fioArr[1].trim(),
									firstName = fioArr[2].trim(),
									patronymic = fioArr[3].trim()
								)
							}

							3 -> {
								FullName(
									lastName = fioArr[0].trim(),
									firstName = fioArr[1].trim(),
									patronymic = fioArr[2].trim()
								)
							}

							2 -> FullName(
								lastName = fioArr[0].trim(),
								firstName = fioArr[1].trim(),
							)

							else -> {
								// Пустое ФИО, или имен больше
								addReport.add(
									AddUserReport(
										success = false,
										userDetails = UserDetails(
											user = User(
												lastname = fio,
												dept = currentDept
											)
										),
										isUpdate = false,
										errors = listOf(
											errorValidation(
												field = "fio",
												violationCode = "not valid",
												description = "Не верный формат ФИО",
												level = ContextError.Levels.ERROR
											)
										)
									)
								)
								continue
							}
						}

						val tabId = colTabId?.let { row.getCellText(it).toLongOrNull() }
						val post = colPost?.let { row.getCellText(it).trim() }
						val phone = colPhone?.let { row.getCellText(it).trim() }
						val schedule = colSchedule?.let { row.getCellText(it).trim() }

						val emailNotVerify = colEmail?.let { row.getCellText(it).trim() }
						val email = emailNotVerify?.let {
							if (isValidEmail(it)) {
								it
							} else {
								userErrors.add(parseEmailError(it))
								null
							}
						}

						val birthDate = colBirthData?.let {
							val cell = row.getCell(it)
							cellToDate(cell, FIELD_BIRTH_DATE)
						}

						val jobDate = colJobDate?.let {
							val cell = row.getCell(it)
							cellToDate(cell, FIELD_JOB_DATE)
						}

						userDetails = UserDetails(
							user = User(
								authEmail = email,
								firstname = fullName.firstName,
								lastname = fullName.lastName,
								patronymic = fullName.patronymic,
								roles = setOf(RoleUser.USER),
								post = post,
								dept = currentDept
							),
							phone = phone,
							schedule = schedule,
							tabId = tabId,
							description = "Профиль загружен автоматически из Excel"
						)

						if (currentDept == null) {
							// Отдел не определен
							addReport.add(
								AddUserReport(
									success = false,
									userDetails = userDetails,
									isUpdate = false,
									errors = listOf(
										errorValidation(
											field = "dept",
											violationCode = "undefined",
											description = "Для Сотрудника не определен Отдел",
											level = ContextError.Levels.ERROR
										)
									)
								)
							)
							continue
						}

						// Если Отдел найден, то ищем Сотрудника в нем
						val findUserDetails = when {
							tabId != null && updateKey == UpdateKey.USER_TAB_NO -> {
								try {
									userService.findIdByTabIdAndDeptId(
										tabId = tabId,
										deptId = currentDept.id
									)
								} catch (e: Exception) {
									log.error(e.message)
									throw UserIOException()
								}
							}

							else -> try {
								userService.findIdByFullNameAndDeptsIds(
									fullName = fullName,
									deptsIds = deptsIds
								)
							} catch (e: Exception) {
								log.error(e.message)
								throw UserIOException()
							}
						}

						/**
						 * Обновление профиля Сотрудника
						 */
						if (findUserDetails != null
						) {
							userDetails = userDetails.copy(
								user = userDetails.user.copy(
									id = findUserDetails.user.id,
								)
							)

							if (!(
										(findUserDetails.user.authEmail == email || email.isNullOrBlank()) &&
												findUserDetails.user.firstname == userDetails.user.firstname &&
												findUserDetails.user.lastname == userDetails.user.lastname &&
												findUserDetails.user.patronymic == userDetails.user.patronymic &&
												findUserDetails.user.post == userDetails.user.post &&
												findUserDetails.phone == userDetails.phone &&
												findUserDetails.tabId == userDetails.tabId &&
												findUserDetails.user.dept?.id == currentDept.id
										)
							) {
								try {
									userService.updateFromExcel(userDetails)
									isUpdate = true
								} catch (e: Exception) {
									log.error(e.message)
									throw UserIOException()
								}
							}

							isUpdate = addOrUpdateUserEvent(
								userId = findUserDetails.user.id,
								birthDate = birthDate,
								jobDate = jobDate,
								userEvents = userEvents,
								userErrors = userErrors,
							) || isUpdate

							if (isUpdate) updatedUserCount++

						} else {
							/**
							 * Создание нового профиля Сотрудника
							 */
							try {
								userService.create(userDetails)
								createdUserCount++
							} catch (e: Exception) {
								log.error(e.message)
								throw UserIOException()
							}
						}

						addReport.add(
							AddUserReport(
								success = true,
								userDetails = userDetails.copy(user = userDetails.user.copy(dept = currentDept)),
								isUpdate = isUpdate,
								events = userEvents,
								errors = userErrors
							)
						)

						// Если первый символ - не число
					} else {
						/**
						 * Добавление/Обновление Отдела
						 */
						val currentDeptName = row.getCellText(0)
						if (currentDeptName.isNullOrBlank()) continue
//						isDeptFound = true
						currentDept = try {
							deptService.findByIdsAndName(ids = deptsIds, name = currentDeptName).firstOrNull() ?: run {
//								isDeptFound = false
								// Если не находим, то создаем новый
								val deptDetails = DeptDetails(
									dept = Dept(
										name = currentDeptName,
										parentId = deptId,
										type = DeptType.SIMPLE
									),
									description = "Отдел создан автоматически из Excel файла"
								)
								val newDeptDetails = deptService.create(deptDetails)
								createdDeptCount++
								newDeptDetails.dept
							}
						} catch (e: Exception) {
							throw DeptIOException()
						}
					}
				}

				loadReport = LoadReport(
					addReport = addReport,
					createdDeptCount = createdDeptCount,
					createdUserCount = createdUserCount,
					updatedUserCount = updatedUserCount
				)

			}
		}
	}

	except {
		log.error(it.message)

		when (it) {
			is UserIOException -> userDbError()
			is DeptIOException -> deptDbError()
			is EventIOException -> userEventError()
			is ParseExcelException -> excelFormatError(it.message)
			else -> excelFormatError()
		}

	}
}