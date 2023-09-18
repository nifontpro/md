@file:Suppress("KDocUnresolvedReference")

package ru.md.msc.rest.user

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.rest.BaseResponse
import ru.md.base_rest.authProcess
import ru.md.base_rest.model.request.AUTH
import ru.md.base_rest.toLongOr0
import ru.md.base_rest.utils.JwtUtils
import ru.md.msc.domain.user.biz.proc.UserCommand
import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.biz.proc.UserProcessor
import ru.md.msc.domain.user.model.GenderCount
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserSettings
import ru.md.msc.rest.base.imageProcess
import ru.md.msc.rest.base.mappers.toTransportBaseImage
import ru.md.msc.rest.base.mappers.toTransportUnit
import ru.md.msc.rest.user.mappers.*
import ru.md.msc.rest.user.model.request.*
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
	): BaseResponse<UserDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserDetails() }
		)
	}

	@PostMapping("create")
	private suspend fun createUser(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: CreateUserRequest
	): BaseResponse<UserDetailsResponse> {
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
	): BaseResponse<List<User>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUsers() }
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
	): BaseResponse<List<User>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUsers() }
		)
	}

	@PostMapping("exclude_by_depts")
	private suspend fun getBySubDeptsExclude(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUsersByDeptsExcludeRequest
	): BaseResponse<List<User>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUsers() }
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
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserDetails() }
		)
	}

	@PostMapping("img_add")
	suspend fun imageAdd(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestPart("file") file: MultipartFile,
		@RequestPart("authId") authId: String,
		@RequestPart("userId") userId: String,
	): BaseResponse<BaseImage> {
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		val context = UserContext().apply { command = UserCommand.IMG_ADD }
		return imageProcess(
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
	): BaseResponse<BaseImage> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseImage() }
		)
	}

	@PostMapping("gender_count")
	private suspend fun getGenderCountByDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetGenderCountByDeptRequest
	): BaseResponse<GenderCount> {
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
	): BaseResponse<List<UserResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUsersResponse() }
		)
	}

	@PostMapping("get_awards_na")
	private suspend fun getWithAwards(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUsersWithAwardsRequest
	): BaseResponse<List<UserResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUsersResponse() }
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
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUserSettings() }
		)
	}

	/**
	 * !!!! Set ADMIN role
	 */
	@PostMapping("admin/img")
	private suspend fun setMainImages(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: SetMainUserImagesRequest
	): BaseResponse<Unit> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUnit() }
		)
	}

	@PostMapping("has_owner")
	private suspend fun hasUserOwnerRole(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: HasUserOwnerRequest
	): BaseResponse<Boolean> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportHasOwner() }
		)
	}

	@PostMapping("data")
	suspend fun getData(
		@RequestBody body: RS? = null,
		principal: Principal,
	): RS {
		val usedMb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576
		return RS(res = "User data valid, body: ${body?.res}, used: $usedMb Mb, ${principal.name}")
	}

}

data class RS(
	val res: String
)