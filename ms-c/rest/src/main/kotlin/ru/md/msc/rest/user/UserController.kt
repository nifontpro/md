package ru.md.msc.rest.user

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.rest.BaseResponse
import ru.md.base_rest.authProcess
import ru.md.base_rest.model.AUTH
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
import ru.md.msc.rest.base.mappers.toTransportDeptId
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

	@PostMapping("get_by_dept")
	private suspend fun getByDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUsersByDeptRequest
	): BaseResponse<List<User>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUsers() }
		)
	}

	@PostMapping("get_by_subdepts")
	private suspend fun getBySubDepts(
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
		@RequestPart("userId") userId: String,
	): BaseResponse<BaseImage> {
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		val context = UserContext().apply { command = UserCommand.IMG_ADD }
		val entityId = userId.toLongOr0()
		return imageProcess(
			authData = authData,
			context = context,
			processor = userProcessor,
			multipartFile = file,
			authId = entityId,
			entityId = entityId,
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
	 * Получить сотрудников с количеством награждений
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
	 * Получение id отдела авторизованного пользователя
	 */
	@PostMapping("get_dept_id")
	private suspend fun getAuthParentId(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetAuthDeptIdRequest
	): BaseResponse<Long> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = userProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptId() }
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