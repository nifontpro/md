package ru.md.msc.rest.award

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.gallery.SmallItem
import ru.md.base_domain.model.BaseResponse
import ru.md.base_rest.base.authProcess
import ru.md.base_rest.base.process
import ru.md.base_rest.base.toLongOr0
import ru.md.base_rest.image.baseImageProcess
import ru.md.base_rest.logEndpoint
import ru.md.base_rest.logRequest
import ru.md.base_rest.model.mapper.toTransportBaseImageResponse
import ru.md.base_rest.model.request.AUTH
import ru.md.base_rest.model.response.BaseImageResponse
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
		log.info(logEndpoint("create"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("update"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("get_id"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwardDetails() }
		)
	}

	/**
	 * Получение наград из отдела deptId (Награды с периодом выводятся только попадающие в период)
	 * state: AwardState? - фильтрация по состоянию награды
	 * withUsers: Boolean - Включать ли информацию о награжденных (по умолчанию false),
	 *  использовать при крайней необходимости.
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
		log.info(logEndpoint("get_dept"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("get_subdepts"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("admin_available_user_exclude"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("get_simple"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("delete"))
		log.info(logRequest(request))
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
	): BaseResponse<BaseImageResponse> {
		log.info(logEndpoint("img_add"))
		log.info("Request: awardId=$awardId")
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		val context = AwardContext().apply { command = AwardCommand.IMG_ADD }
		return baseImageProcess(
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
	): BaseResponse<BaseImageResponse> {
		log.info(logEndpoint("img_delete"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseImageResponse() }
		)
	}

	@PostMapping("img_gal")
	private suspend fun imageAddFromGallery(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: AddAwardImageFromGalleryRequest
	): BaseResponse<BaseImageResponse> {
		log.info(logEndpoint("img_gal"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseImageResponse() }
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
		log.info(logEndpoint("action"))
		log.info(logRequest(request))
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
	 * фильтры: awardType, awardState
	 * 	baseRequest:
	 * 	Пагинация,
	 * 	minDate, maxDate - фильтрация по дате
	 * 	filter - фильтрация по названию наград
	 * 	Допустимые поля для сортировки:
	 * 	     	"date",
	 * 				"actionType",
	 * 				"award.name",
	 * 				"award.type",
	 * 				"award.startDate",
	 * 				"award.endDate",
	 */
	@PostMapping("act_user")
	private suspend fun getActivAwardByUser(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetActivAwardByUserRequest
	): BaseResponse<List<ActivityResponse>> {
		log.info(logEndpoint("act_user"))
		log.info(logRequest(request))
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
	 * 	subdepts - отдел или все подотделы
	 * 	Допустимые поля для сортировки:
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
		log.info(logEndpoint("count_dep"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("act_award"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("count_dep"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("count_activ"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("count_activ_root"))
		log.info(logRequest(request))
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
		log.info(logEndpoint("count_user_ww"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = awardProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportWWAwardsCount() }
		)
	}

	@PostMapping("get_item")
	private suspend fun getGalleryItem(
		@RequestBody request: GetGalleryRequest
	): BaseResponse<SmallItem> {
		log.info(logEndpoint("get_item"))
		log.info(logRequest(request))
		return process(
			processor = awardProcessor,
			request = request,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportSmallItem() }
		)
	}

	/**
	 * Для процесса разработки! Удалить в релизе.
	 * !!!! Set ADMIN role
	 * Обновление основных изображений у медалей
	 */
//	@PostMapping("admin/img")
//	private suspend fun setMainImages(
//		@RequestHeader(name = AUTH) bearerToken: String,
//		@RequestBody request: SetMainAwardImagesRequest
//	): BaseResponse<Unit> {
//		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
//		return authProcess(
//			processor = awardProcessor,
//			authRequest = baseRequest,
//			fromTransport = { fromTransport(it) },
//			toTransport = { toTransportUnit() }
//		)
//	}

	companion object {
		private val log: Logger = LoggerFactory.getLogger(AwardController::class.java)
	}

}