package ru.md.msc.domain.user.biz.workers

import org.dhatim.fastexcel.reader.ReadableWorkbook
import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.errorValidation
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.helper.otherError
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
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.UserIOException
import ru.md.msc.domain.user.biz.proc.userDbError
import ru.md.msc.domain.user.biz.proc.userEventError
import ru.md.msc.domain.user.model.FullName
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.domain.user.model.excel.AddUserReport
import ru.md.msc.domain.user.model.excel.UpdateKey
import java.io.FileInputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
					throw Exception("Слишком мало строк: $maxRowIdx")
				}

				/**
				 * Определение позиций основных полей
				 */
				var colFioNull: Int? = null
				var colTabId: Int? = null
				var colPost: Int? = null
				var colPhone: Int? = null
				var colBirthData: Int? = null
				var colJobDate: Int? = null

				run blockEach@{
					rows.forEachIndexed { idx, row ->
						currentRow = idx
						val ch = row.getCellText(0)
						if (ch == "№") {
							row.getCells(1, row.cellCount).forEach { cell ->
								val rowIdx = cell.columnIndex
								log.info("$rowIdx. ${cell.text}")
								when (cell.text) {
									textFio -> colFioNull = rowIdx
									textTabId -> colTabId = rowIdx
									textPost -> colPost = rowIdx
									textPhone -> colPhone = rowIdx
									textBirthday -> colBirthData = rowIdx
									textJobDate -> colJobDate = rowIdx
								}
							}
							return@blockEach
						}
					}
				}

				if (colFioNull == null) throw ParsePosFIOException("Не определена позиция ФИО")
				val colFio = colFioNull ?: 0

				var currentDept: Dept? = null
				var isDeptFound = false

				val deptsIds = baseDeptService.findSubTreeIds(deptId)

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
						if (currentDept == null) {
							// Отдел не определен
							addReport.add(
								AddUserReport(
									success = false,
									userDetails = null,
									isUpdate = false,
									errors = listOf(
										errorValidation(
											field = "dept",
											violationCode = "undefined",
											description = "Не определен Отдел"
										)
									)
								)
							)
							continue
						}

						val fio = row.getCellText(colFio)
						val fioArr = fio.split(" ")

						val fullName = when (fioArr.size) {
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

							1 -> FullName(
								lastName = fioArr[0].trim(),
							)

							else -> {
								// Пустое ФИО, или имен больше
								addReport.add(
									AddUserReport(
										success = false,
										userDetails = null,
										isUpdate = false,
										errors = listOf(
											errorValidation(
												field = "fio",
												violationCode = "not valid",
												description = "Не верный формат ФИО"
											)
										)
									)
								)
								continue
							}
						}

						val tabId = colTabId?.let { row.getCellText(it).toLongOrNull() }
						val post = colPost?.let { row.getCellText(it) }
						val phone = colPhone?.let { row.getCellText(it) }
						val birthDateStr = colBirthData?.let { row.getCellText(it) }
						val jobDateStr = colJobDate?.let { row.getCellText(it) }

						userDetails = UserDetails(
							user = User(
								firstname = fullName.firstName,
								lastname = fullName.lastName,
								patronymic = fullName.patronymic,
								roles = setOf(RoleUser.USER),
								post = post,
								dept = currentDept
							),
							phone = phone,
							tabId = tabId,
							description = "Профиль загружен автоматически из Excel"
						)

						if (isDeptFound) {
							// Если Отдел найден, то ищем Сотрудника в нем
							val findUserId = when {
								tabId != null && updateKey == UpdateKey.USER_TAB_NO -> {
									try {
										userService.findIdByTabIdAndDeptId(
											tabId = tabId,
											deptId = currentDept.id
										)
									} catch (e: Exception) {
										throw UserIOException()
									}
								}

								else -> try {
									userService.findIdByFullNameAndDeptId(
										fullName = fullName,
										deptId = currentDept.id
									)
								} catch (e: Exception) {
									throw UserIOException()
								}
							}

							/**
							 * Обновление профиля Сотрудника
							 */
							if (findUserId != null) {
								isUpdate = true
								userDetails = userDetails.copy(user = userDetails.user.copy(id = findUserId))
								try {
									userService.updateFromExcel(userDetails)
								} catch (e: Exception) {
									throw UserIOException()
								}

								birthDateStr?.let {
									val date = try {
										it.toDate()
									} catch (e: Exception) {
										userErrors.add(parseDateError(it))
										return@let
									}
									val userEvent = UserEvent(
										eventDate = date,
										eventName = dbBirthday,
										userId = findUserId
									)
									try {
										userEvents.add(eventService.addOrUpdateUserEvent(userEvent))
									} catch (e: Exception) {
										throw EventIOException()
									}
								}

								jobDateStr?.let {
									val date = try {
										it.toDate()
									} catch (e: Exception) {
										userErrors.add(parseDateError(it))
										return@let
									}
									val userEvent = UserEvent(
										eventDate = date,
										eventName = dbJobDate,
										userId = findUserId
									)
									try {
										userEvents.add(eventService.addOrUpdateUserEvent(userEvent))
									} catch (e: Exception) {
										throw EventIOException()
									}
								}

							} else {
								/**
								 * Создание нового профиля Сотрудника
								 */
								try {
									userService.create(userDetails)
								} catch (e: Exception) {
									throw UserIOException()
								}
							}

						} else {
							// Если был создан новый Отдел, то добавляем Сотрудника в него
							try {
								userDetails = userService.create(userDetails)
							} catch (e: Exception) {
								throw UserIOException()
							}

							// Добавляем события
							birthDateStr?.let {
								val date = try {
									it.toDate()
								} catch (e: Exception) {
									userErrors.add(parseDateError(it))
									return@let
								}
								val userEvent = UserEvent(
									eventDate = date,
									eventName = dbBirthday,
									userId = userDetails.user.id
								)
								try {
									userEvents.add(eventService.addOrUpdateUserEvent(userEvent))
								} catch (e: Exception) {
									throw EventIOException()
								}
							}

							jobDateStr?.let {
								val date = try {
									it.toDate()
								} catch (e: Exception) {
									userErrors.add(parseDateError(it))
									return@let
								}
								val userEvent = UserEvent(
									eventDate = date,
									eventName = dbJobDate,
									userId = userDetails.user.id
								)
								try {
									userEvents.add(eventService.addOrUpdateUserEvent(userEvent))
								} catch (e: Exception) {
									throw EventIOException()
								}
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

					} else {
						/**
						 * Добавление/Обновление Отдела
						 */
						val currentDeptName = row.getCellText(0)
						if (currentDeptName.isNullOrBlank()) continue
						isDeptFound = true
						currentDept = try {
							deptService.findByIdsAndName(ids = deptsIds, name = currentDeptName).firstOrNull() ?: run {
								isDeptFound = false
								// Если не находим, то создаем новый
								val deptDetails = DeptDetails(
									dept = Dept(
										name = currentDeptName,
										parentId = deptId,
										type = DeptType.SIMPLE
									),
									description = "Отдел создан автоматически из Excel файла"
								)
								deptService.create(deptDetails).dept
							}
						} catch (e: Exception) {
							throw DeptIOException()
						}
					}
				}

			}
		}
	}

	except {
		log.error(it.message)

		when (it) {
			is UserIOException -> userDbError()
			is DeptIOException -> deptDbError()
			is EventIOException -> userEventError()
			is ParsePosFIOException -> fail(
				otherError(
					description = "Не определена позиция ФИО",
					field = "fio",
					code = "undefined pos fio",
					level = ContextError.Levels.ERROR
				)
			)

			else -> fail(
				otherError(
					description = "Ошибка чтения Excel файла",
					field = "excel",
					code = "excel-read error",
					level = ContextError.Levels.ERROR
				)
			)
		}

	}
}

const val textFio = "Сотрудник"
const val textTabId = "Табельный номер"
const val textPost = "Должность"
const val textPhone = "Телефон рабочий"
const val textBirthday = "Дата рождения"
const val dbBirthday = "День рождения"
const val textJobDate = "Дата приема"
const val dbJobDate = "Прием на работу"

private class ParsePosFIOException(message: String?) : RuntimeException(message)

private fun parseDateError(date: String) = errorValidation(
	field = "date",
	violationCode = "not valid",
	description = "Неверный формат даты: $date"
)


fun String.isInt() = this.toIntOrNull()?.let { true } ?: false

fun String.toDate(): LocalDateTime {
	return LocalDate.parse(this, DateTimeFormatter.ofPattern("dd.MM.yyyy")).atStartOfDay()
}