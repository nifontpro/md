package ru.md.msc.domain.user.biz.workers

import org.dhatim.fastexcel.reader.ReadableWorkbook
import ru.md.base_domain.biz.helper.ContextError
import ru.md.base_domain.biz.helper.fail
import ru.md.base_domain.biz.helper.otherError
import ru.md.base_domain.biz.proc.ContextState
import ru.md.base_domain.dept.biz.errors.DeptIOException
import ru.md.base_domain.dept.model.Dept
import ru.md.base_domain.dept.model.DeptType
import ru.md.base_domain.user.model.RoleUser
import ru.md.base_domain.user.model.User
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.event.model.BaseEvent
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.FullName
import ru.md.msc.domain.user.model.UserDetails
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
						log.info("--> i: $currentRow, ch: $ch")
						if (ch == "№") {
							log.warn("Ключ № найден!")
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

				log.info("colFio = $colFioNull")
				log.info("colTabId = $colTabId")
				log.info("colPost = $colPost")
				log.info("colPhone = $colPhone")
				log.info("colBirthData = $colBirthData")
				log.info("colJobDate = $colJobDate")

				if (colFioNull == null) throw Exception("Не определена позиция ФИО")
				val colFio = colFioNull ?: 0

				var currentDeptId = 0L
				var isDeptFound = false

//				deptId = authUser.dept?.id ?: throw Exception("Отдел не найден")
//				val companyId = baseDeptService.getCompanyDeptId(deptId)
				// deptId - отдел компании, полученный выше по цепочке
				val deptsIds = baseDeptService.findSubTreeIds(deptId)

				for (i in ++currentRow..maxRowIdx) {
					val row = rows[i]
					val firstRowText = row.getCellText(0)
					if (firstRowText.isNullOrBlank()) continue // Пустая строка пропускается

					if (firstRowText.isInt()) {
						/**
						 * Добавление/Обновление Сотрудника
						 */
						if (currentDeptId == 0L) continue // Отдел не определен

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

							else -> continue // Пустое ФИО, или имен больше
						}

						val tabId = colTabId?.let { row.getCellText(it).toLongOrNull() }
						val post = colPost?.let { row.getCellText(it) }
						val phone = colPhone?.let { row.getCellText(it) }
						val birthDateStr = colBirthData?.let { row.getCellText(it) }
//						val jobDateStr = colJobDate?.let { row.getCellText(it) }

						userDetails = UserDetails(
							user = User(
								firstname = fullName.firstName,
								lastname = fullName.lastName,
								patronymic = fullName.patronymic,
								roles = setOf(RoleUser.USER),
								post = post,
								dept = Dept(id = currentDeptId)
							),
							phone = phone,
							tabId = tabId,
							description = "Профиль загружен автоматически из Excel"
						)

						if (isDeptFound) {
							// Если Отдел найден, то ищем Сотрудника в нем
							val findUserId = when {
								tabId != null && updateKey == UpdateKey.USER_TAB_NO -> userService.findIdByTabIdAndDeptId(
									tabId = tabId,
									deptId = currentDeptId
								)

								else -> userService.findIdByFullNameAndDeptId(
									fullName = fullName,
									deptId = currentDeptId
								)
							}

							if (findUserId != null) {
								userDetails = userDetails.copy(user = userDetails.user.copy(id = findUserId))
								log.info("Обновляем профиль сотрудника \n$userDetails")
								userService.updateFromExcel(userDetails)

								birthDateStr?.let {
									val date = it.toDate() // T-C
									val baseEvent = BaseEvent(
										eventDate = date,
										eventName = textBirthday,
										userId = findUserId
									)
									eventService.addOrUpdateUserEvent(baseEvent)
								}

							} else {
								log.info("Создаем сотрудника \n$userDetails")
								userService.create(userDetails)
							}

						} else {
							// Если был создан новый Отдел, то добавляем Сотрудника в него
							log.info("Создаем сотрудника\n$userDetails")
							userService.create(userDetails)
							// Добавляем события
						}


					} else {
						/**
						 * Добавление/Обновление Отдела
						 */
						val currentDeptName = row.getCellText(0)
						if (currentDeptName.isNullOrBlank()) continue
						isDeptFound = true
						val dept = try {
							deptService.findByIdsAndName(ids = deptsIds, name = currentDeptName).firstOrNull() ?: run {
								isDeptFound = false
								// Если не находим, то создаем новый
								val deptDetails = DeptDetails(
									dept = Dept(
										name = currentDeptName,
										parentId = deptId,
										type = DeptType.SIMPLE
									),
									description = "Отдел создан автоматически на основе Excel файла"
								)
								log.info("Создаем новый отдел $currentDeptName")
								deptService.create(deptDetails).dept
							}
						} catch (e: Exception) {
							throw DeptIOException()
						}

						log.info("i: $i, dept: $dept")
						currentDeptId = dept.id
						log.warn("currentDeptId: $currentDeptId")
					}
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

const val textFio = "Сотрудник"
const val textTabId = "Табельный номер"
const val textPost = "Должность"
const val textPhone = "Телефон рабочий"
const val textBirthday = "Дата рождения"
const val textJobDate = "Дата приема"

fun String.isInt() = this.toIntOrNull()?.let { true } ?: false

fun String.toDate(): LocalDateTime {
	return LocalDate.parse(this, DateTimeFormatter.ofPattern("dd.MM.yyyy")).atStartOfDay()
}