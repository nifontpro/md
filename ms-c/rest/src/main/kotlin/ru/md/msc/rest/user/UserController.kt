@file:Suppress("KDocUnresolvedReference")

package ru.md.msc.rest.user

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.model.BaseResponse
import ru.md.base_rest.base.authProcess
import ru.md.base_rest.base.toLongOr0
import ru.md.base_rest.image.baseImageProcess
import ru.md.base_rest.logEndpoint
import ru.md.base_rest.logRequest
import ru.md.base_rest.model.mapper.toTransportBaseImageResponse
import ru.md.base_rest.model.request.AUTH
import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.base_rest.utils.JwtUtils
import ru.md.base_rest.utils.testResponse
import ru.md.msc.domain.user.biz.proc.UserCommand
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.UserProcessor
import ru.md.msc.domain.user.model.GenderCount
import ru.md.msc.domain.user.model.UserSettings
import ru.md.msc.domain.user.model.excel.LoadReport
import ru.md.msc.rest.user.excel.excelProcess
import ru.md.msc.rest.user.mappers.*
import ru.md.msc.rest.user.model.request.*
import ru.md.msc.rest.user.model.response.UserAwardResponse
import ru.md.msc.rest.user.model.response.UserDetailsDeptResponse
import ru.md.msc.rest.user.model.response.UserDetailsResponse
import ru.md.msc.rest.user.model.response.UserResponse
import java.security.Principal

@RestController
@RequestMapping("user")
class UserController(
	private val userProcessor: UserProcessor,
	private val jwtUtils: JwtUtils,
) {

	@PostMapping("create_owner")
	private suspend fun createOwner(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: CreateOwnerRequest
	): BaseResponse<UserDetailsDeptResponse> {
		log.info(logEndpoint("data"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserDetailsDept() }
		)
	}

	@PostMapping("create")
	private suspend fun createUser(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: CreateUserRequest
	): BaseResponse<UserDetailsResponse> {
		log.info(logEndpoint("create"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserDetails() }
		)
	}

	@PostMapping("update")
	private suspend fun updateUser(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: UpdateUserRequest
	): BaseResponse<UserDetailsResponse> {
		log.info(logEndpoint("update"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserDetails() }
		)
	}

	@PostMapping("profiles")
	private suspend fun getProfiles(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetProfilesRequest
	): BaseResponse<List<UserResponse>> {
		log.info(logEndpoint("profiles"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUsersResponse() }
		)
	}

	/**
	 * Получение сотрудников отдела [deptId]
	 * [baseRequest]:
	 *    subdepts - отдел или все подотделы
	 *    Параметры пагинации [page], [pageSize]
	 *    Параметр [filter] - фильтрация по Фамилии сотрудника
	 *    Допустимые поля для сортировки:
	 *          "firstname",
	 *    			"patronymic",
	 *    			"lastname",
	 *    			"authEmail",
	 *    			"post",
	 *          "dept.name" - Первым рекомендую его установить,
	 *     			"dept.classname",
	 */
	@PostMapping("get_by_dept")
	private suspend fun getByDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUsersBySubDeptsRequest
	): BaseResponse<List<UserResponse>> {
		log.info(logEndpoint("get_by_dept"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUsersResponse() }
		)
	}

	@PostMapping("exclude_by_depts")
	private suspend fun getBySubDeptsExclude(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUsersByDeptsExcludeRequest
	): BaseResponse<List<UserResponse>> {
		log.info(logEndpoint("exclude_by_depts"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUsersResponse() }
		)
	}

	/**
	 * Удаление профиля сотрудника
	 * forever: Boolean - true: удаление навсегда со связанными данными
	 * 									- false: помещение профиля в архив
	 */
	@PostMapping("delete")
	private suspend fun deleteUser(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: DeleteUserRequest
	): BaseResponse<UserDetailsResponse> {
		log.info(logEndpoint("delete"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserDetails() }
		)
	}

	@PostMapping("get_id")
	private suspend fun getUserById(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUserByIdRequest
	): BaseResponse<UserDetailsResponse> {
		log.info(logEndpoint("get_id"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserDetails() }
		)
	}

	@PostMapping("excel_add")
	private suspend fun addFromExcel(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestPart("file") file: MultipartFile,
		@RequestPart("authId") authId: String,
		@RequestPart("deptId") deptId: String,
	): BaseResponse<LoadReport> {
		log.info(logEndpoint("excel_add"))
		log.info(logRequest(deptId))
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		return excelProcess(
			authData = authData,
			authId = authId.toLongOr0(),
			deptId = deptId.toLongOr0(),
			processor = userProcessor,
			multipartFile = file,
		)
	}

	@PostMapping("img_add")
	suspend fun imageAdd(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestPart("file") file: MultipartFile,
		@RequestPart("authId") authId: String,
		@RequestPart("userId") userId: String,
	): BaseResponse<BaseImageResponse> {
		log.info(logEndpoint("img_add"))
		log.info(logRequest(userId))
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		val context = UserContext().apply { command = UserCommand.IMG_ADD }
		return baseImageProcess(
			authData = authData,
			context = context,
			processor = userProcessor,
			multipartFile = file,
			authId = authId.toLongOr0(),
			entityId = userId.toLongOr0(),
		)
	}

	@PostMapping("img_delete")
	private suspend fun imageDelete(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: DeleteUserImageRequest
	): BaseResponse<BaseImageResponse> {
		log.info(logEndpoint("img_delete"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseImageResponse() }
		)
	}

	@PostMapping("gender_count")
	private suspend fun getGenderCountByDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetGenderCountByDeptRequest
	): BaseResponse<GenderCount> {
		log.info(logEndpoint("gender_count"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportGenderCount() }
		)
	}

	@PostMapping("get_act_na")
	private suspend fun getWithActivity(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUsersWithActivityRequest
	): BaseResponse<List<UserAwardResponse>> {
		log.info(logEndpoint("get_act_na"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUsersAwardsResponse() }
		)
	}

	@PostMapping("get_awards_na")
	private suspend fun getWithAwards(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUsersWithAwardsRequest
	): BaseResponse<List<UserAwardResponse>> {
		log.info(logEndpoint("get_awards_na"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUsersAwardsResponse() }
		)
	}

	/**
	 * Получить сотрудников с количеством награждений и общим числом баллов
	 * baseRequest:
	 *  subdepts - отдел или все подотделы
	 *  Параметры пагинации page, pageSize - необязательны, по умолчанию 0 и 100 соответственно
	 *  minDate <= activity.startDate (отсутствует - без min ограничения)
	 *  maxDate >= activity.endDate (отсутствует - без max ограничения) - для подсчета наград за период
	 *  Допустимые поля для сортировки:
	 *      "firstname",
	 *      "patronymic",
	 *      "lastname",
	 *      "post",
	 *      "(awardCount)",
	 *      "(scores)",
	 *      "(deptName)",
	 *      "(classname)",
	 */
	@PostMapping("get_award_count")
	private suspend fun getWithAwardCount(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUsersWithAwardCountRequest
	): BaseResponse<List<UserResponse>> {
		log.info(logEndpoint("get_award_count"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUsersResponse() }
		)
	}

	@PostMapping("save_settings")
	private suspend fun saveUserSettings(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: SaveUserSettingsRequest
	): BaseResponse<UserSettings> {
		log.info(logEndpoint("save_settings"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserSettings() }
		)
	}

	@PostMapping("get_settings")
	private suspend fun getUserSettings(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUserSettingsRequest
	): BaseResponse<UserSettings> {
		log.info(logEndpoint("get_settings"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserSettings() }
		)
	}

	@PostMapping("has_owner")
	private suspend fun hasUserOwnerRole(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: HasUserOwnerRequest
	): BaseResponse<Boolean> {
		log.info(logEndpoint("has_owner"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportHasOwner() }
		)
	}

	/**
	 * !!!! Set ADMIN role
	 */
//	@PostMapping("admin/img")
//	private suspend fun setMainImages(
//		@RequestHeader(name = AUTH) bearerToken: String,
//		@RequestBody request: SetMainUserImagesRequest
//	): BaseResponse<Unit> {
//		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
//		return authProcess(
//			processor = userProcessor,
//			authRequest = baseRequest,
//			fromTransport = { fromTransport(it) },
//			toTransport = { toTransportUnit() }
//		)
//	}

//		@PostMapping("admin/gender")
//	private suspend fun addMaleName(
//		@RequestHeader(name = AUTH) bearerToken: String,
//		@RequestBody request: AddGenderRequest
//	): BaseResponse<Unit> {
//		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
//		return authProcess(
//			processor = userProcessor,
//			authRequest = baseRequest,
//			fromTransport = { fromTransport(it) },
//			toTransport = { toTransportUnit() }
//		)
//	}

	@PostMapping("data")
	suspend fun getData(
		@RequestBody body: RS? = null,
		principal: Principal,
	): RS {
		log.info(logEndpoint("data"))
		log.info(logRequest(body))
		val usedMb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576
		return RS(res = "User data valid, body: ${body?.res}, used: $usedMb Mb, ${principal.name}")
	}

	@GetMapping("test")
	suspend fun test() = testResponse()

	companion object {
		private val log: Logger = LoggerFactory.getLogger(UserController::class.java)
	}

}

data class RS(
	val res: String
)