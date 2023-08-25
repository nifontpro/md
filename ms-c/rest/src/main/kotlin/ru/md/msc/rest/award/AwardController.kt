package ru.md.msc.rest.award

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.rest.BaseResponse
import ru.md.base_rest.authProcess
import ru.md.base_rest.model.AUTH
import ru.md.base_rest.toLongOr0
import ru.md.base_rest.utils.JwtUtils
import ru.md.msc.domain.award.biz.proc.AwardCommand
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.AwardProcessor
import ru.md.msc.domain.award.model.AwardCount
import ru.md.msc.domain.award.model.AwardStateCount
import ru.md.msc.domain.award.model.WWAwardCount
import ru.md.msc.rest.award.mappers.*
import ru.md.msc.rest.award.model.request.*
import ru.md.msc.rest.award.model.response.ActivityResponse
import ru.md.msc.rest.award.model.response.AwardDetailsResponse
import ru.md.msc.rest.award.model.response.AwardResponse
import ru.md.msc.rest.base.imageProcess
import ru.md.msc.rest.base.mappers.toTransportBaseImage
import ru.md.msc.rest.base.mappers.toTransportUnit

@RestController
@RequestMapping("award")
class AwardController(
	private val awardProcessor: AwardProcessor,
	private val jwtUtils: JwtUtils,
) {

	@PostMapping("create")
	private suspend fun create(
		@RequestBody request: CreateAwardRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<AwardDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwardDetails() }
		)
	}

	@PostMapping("update")
	private suspend fun update(
		@RequestBody request: UpdateAwardRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<AwardDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwardDetails() }
		)
	}

	@PostMapping("get_id")
	private suspend fun getAwardById(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetAwardByIdRequest
	): BaseResponse<AwardDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwardDetails() }
		)
	}

	/**
	 * Получение наград из отдела deptId
	 * state: AwardState? - фильтрация по состоянию награды
	 * baseRequest:
	 * 	subdepts - отдел или все подотделы
	 *  Допустимые поля для сортировки orders: "name", "type", "startDate", "endDate"
	 *  Пагинация.
	 *  filter - фильтрация по названию (name)
	 */
	@PostMapping("get_dept")
	private suspend fun getAwardByDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetAwardsByDeptRequest
	): BaseResponse<List<AwardResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwards() }
		)
	}

	/**
	 * Получение наград доступных для награждения сотрудников текущим админом
	 * отделы наград берутся из поддерева отделов авторизованного пользователя
	 * Для наград типа AwardType.PERIOD - выводятся только попадающие в период номинации (state=NOMINEE)
	 * baseRequest:
	 *  filter - фильтрация по названию награды (необязателен)
	 *  Параметры пагинации page, pageSize - необязательны, по умолчанию 0 и 100 соответственно
	 *  minDate <= award.startDate (отсутствует - без min ограничения)
	 *  maxDate >= award.endDate (отсутствует - без max ограничения)
	 *  Допустимые поля для сортировки:
	 *  			"name",
	 *  			"type",
	 *  			"startDate",
	 *  			"endDate",
	 *  			"dept.name",
	 *  			"dept.classname",
	 */
	@PostMapping("get_subdepts")
	private suspend fun getAwardBySubDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetAwardsBySubDeptsRequest
	): BaseResponse<List<AwardResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwards() }
		)
	}

	/**
	 * Получение наград доступных для награждения сотрудников текущим админом
	 * с исключением сотрудника userId, который уже имеет эту награду
	 * (исключение действует только для наград с типом PERIOD, наградами SIMPLE можно награждать сколько угодно)
	 * отделы наград берутся из поддерева отделов авторизованного пользователя
	 * Для наград типа AwardType.PERIOD - выводятся только попадающие в период номинации (state=NOMINEE)
	 * baseRequest:
	 *  filter - фильтрация по имени награды (необязателен)
	 *  Параметры пагинации page, pageSize - необязательны, по умолчанию 0 и 100 соответственно
	 *  minDate <= award.startDate (отсутствует - без min ограничения)
	 *  maxDate >= award.endDate (отсутствует - без max ограничения)
	 *  Допустимые поля для сортировки:
	 *  			"name",
	 *  			"type",
	 *  			"startDate",
	 *  			"endDate",
	 *  			"dept.name",
	 *  			"dept.classname",
	 */
	@PostMapping("admin_available_user_exclude")
	private suspend fun getAwardBySubDeptUserExclude(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetAwardsBySubDeptsExcludeUserRequest
	): BaseResponse<List<AwardResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwards() }
		)
	}

	/**
	 * Получение простых наград с awardType == 'SIMPLE',
	 * С вычетом тех, которыми уже награжден сотрудник
	 */
	@PostMapping("get_simple")
	private suspend fun getSimpleAwardAvailable(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetSimpleAwardsAvailableRequest
	): BaseResponse<List<AwardResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwards() }
		)
	}

	@PostMapping("delete")
	private suspend fun delete(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: DeleteAwardRequest
	): BaseResponse<AwardDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwardDetails() }
		)
	}

	@PostMapping("img_add")
	suspend fun imageAdd(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestPart("file") file: MultipartFile,
		@RequestPart("authId") authId: String,
		@RequestPart("awardId") awardId: String,
	): BaseResponse<BaseImage> {
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		val context = AwardContext().apply { command = AwardCommand.IMG_ADD }
		return imageProcess(
			authData = authData,
			context = context,
			processor = awardProcessor,
			multipartFile = file,
			authId = authId.toLongOr0(),
			entityId = awardId.toLongOr0(),
		)
	}

	@PostMapping("img_delete")
	private suspend fun imageDelete(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: DeleteAwardImageRequest
	): BaseResponse<BaseImage> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseImage() }
		)
	}

	@PostMapping("img_gal")
	private suspend fun imageAddFromGallery(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: AddAwardImageFromGalleryRequest
	): BaseResponse<BaseImage> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseImage() }
		)
	}

	/**
	 * Отправить действие actionType с определенно наградой awardId
	 * для сотрудника userId
	 */
	@PostMapping("action")
	private suspend fun sendAction(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: SendActionRequest
	): BaseResponse<ActivityResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportActivity() }
		)
	}

	/**
	 * Получить активные награждения сотрудника userId
	 * baseRequest:
	 * Пагинация,
	 * minDate, maxDate - фильтрация по дате
	 * filter - фильтрация по названию наград
	 * Допустимые поля для сортировки:
	 *      "date",
	 * 			"actionType",
	 * 			"award.name",
	 * 			"award.type"
	 */
	@PostMapping("act_user")
	private suspend fun getActivAwardByUser(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetActivAwardByUserRequest
	): BaseResponse<List<ActivityResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportActivities() }
		)
	}

	/**
	 * Получить активные награждения в отделе deptId
	 * baseRequest:
	 * Допустимые поля для сортировки:
	 *      "date",
	 * 			"actionType",
	 * 			"user.firstname",
	 * 			"user.lastname",
	 * 			"user.patronymic",
	 * 			"user.post",
	 * 			"award.name",
	 * 			"award.type"
	 *
	 *  minDate, maxDate - ограничения по дате событий, необязательны
	 */
	@PostMapping("act_dept")
	private suspend fun getActivAwardByDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetActivAwardByDeptRequest
	): BaseResponse<List<ActivityResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportActivities() }
		)
	}

	/**
	 * Получить сотрудников, награжденных наградой awardId
	 * baseRequest:
	 * Пагинация,
	 * filter - фильтрация по имени и фамилии сотрудников
	 * Допустимые поля для сортировки:
	 *      "date",
	 * 			"actionType",
	 * 			"user.firstname",
	 * 			"user.lastname",
	 * 			"user.patronymic",
	 * 			"user.post",
	 */

	@PostMapping("act_award")
	private suspend fun getUserByActivAward(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUsersByActivAwardRequest
	): BaseResponse<List<ActivityResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportActivities() }
		)
	}

	/**
	 * Получение количества наград в компании
	 * baseRequest:
	 *  subdepts - true: включаются все подотделы
	 *             false: только указанный отдел
	 */
	@PostMapping("count_dep")
	private suspend fun getAwardCountByDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetAwardCountByDeptRequest
	): BaseResponse<AwardStateCount> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwardStateCount() }
		)
	}

	/**
	 * Получение количества активных награждений (наград у пользователей) разных типов в компании
	 * baseRequest:
	 *  Параметры пагинации page, pageSize - необязательны, по умолчанию 0 и 100 соответственно
	 *  subdepts - true: включаются все подотделы
	 *             false: включаются только ближайшие подотделы (у которых parentId=deptId)
	 *  minDate, maxDate - необязательны ограничения по дате события (только для подсчета количества наград)
	 *  Допустимые поля для сортировки:
	 *      !!! В круглых скобках, т.к. используется нативный запрос к БД
	 *      (deptName),
	 *      (awardCount),
	 *      (nomineeCount)
	 */
	@PostMapping("count_activ")
	private suspend fun getActivCountByDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetActivCountByDeptRequest
	): BaseResponse<List<AwardCount>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwardsCount() }
		)
	}

	@PostMapping("count_activ_root")
	private suspend fun getActivCountByRootDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetActivCountByRootDeptRequest
	): BaseResponse<List<AwardCount>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwardsCount() }
		)
	}

	/**
	 * Получение количества сотрудников с наградами и без них
	 * deptId - корневой отдел
	 * baseRequest:
	 *  subdepts - true: включаются все подотделы
	 *             false: включаются только указанный отдел
	 *  minDate, maxDate - (необязательны) ограничения по дате события для подсчета количества наград
	 */
	@PostMapping("count_user_ww")
	private suspend fun getUserAwardWWCount(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetUsersWWAwardCountByDeptRequest
	): BaseResponse<WWAwardCount> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportWWAwardsCount() }
		)
	}

	/**
	 * !!!! Set ADMIN role
	 */
	@PostMapping("admin/img")
	private suspend fun setMainImages(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: SetMainAwardImagesRequest
	): BaseResponse<Unit> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportUnit() }
		)
	}

}