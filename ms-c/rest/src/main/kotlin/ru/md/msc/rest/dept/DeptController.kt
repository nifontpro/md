@file:Suppress("KDocUnresolvedReference")

package ru.md.msc.rest.dept

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.base_domain.model.BaseResponse
import ru.md.base_rest.base.authProcess
import ru.md.base_rest.base.toLongOr0
import ru.md.base_rest.image.baseImageProcessMem
import ru.md.base_rest.logEndpoint
import ru.md.base_rest.logRequest
import ru.md.base_rest.model.mapper.toTransportBaseImageResponse
import ru.md.base_rest.model.request.AUTH
import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.base_rest.utils.JwtUtils
import ru.md.msc.domain.dept.biz.proc.DeptCommand
import ru.md.msc.domain.dept.biz.proc.DeptContext
import ru.md.msc.domain.dept.biz.proc.DeptProcessor
import ru.md.msc.domain.dept.model.DeptSettings
import ru.md.msc.rest.dept.mappers.*
import ru.md.msc.rest.dept.model.request.*
import ru.md.msc.rest.dept.model.response.DeptDetailsResponse
import ru.md.msc.rest.dept.model.response.DeptResponse

@RestController
@RequestMapping("dept")
class DeptController(
	private val deptProcessor: DeptProcessor,
	private val jwtUtils: JwtUtils,
) {

	@PostMapping("create")
	private suspend fun create(
		@RequestBody request: CreateDeptRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<DeptDetailsResponse> {
		log.info(logEndpoint("create"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptDetails() }
		)
	}

	@PostMapping("update")
	private suspend fun update(
		@RequestBody request: UpdateDeptRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<DeptDetailsResponse> {
		log.info(logEndpoint("update"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptDetails() }
		)
	}

	@PostMapping("auth_subtree")
	private suspend fun getAuthSubtreeDepts(
		@RequestBody request: GetAuthSubtreeDeptsRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<List<DeptResponse>> {
		log.info(logEndpoint("auth_subtree"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptsResponse() }
		)
	}

	@PostMapping("top_level_tree")
	private suspend fun getAuthTopLevelTreeDepts(
		@RequestBody request: GetAuthTopLevelTreeDeptsRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<List<DeptResponse>> {
		log.info(logEndpoint("top_level_tree"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptsResponse() }
		)
	}

	@PostMapping("get_id")
	private suspend fun getDeptByIdDetails(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetDeptByIdDetailsRequest
	): BaseResponse<DeptDetailsResponse> {
		log.info(logEndpoint("get_id"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptDetails() }
		)
	}

	@PostMapping("get_id_m")
	private suspend fun getDeptById(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetDeptByIdRequest
	): BaseResponse<DeptResponse> {
		log.info(logEndpoint("get_id_m"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptResponse() }
		)
	}

	@PostMapping("get_auth_dept")
	private suspend fun getAuthDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetAuthDeptRequest
	): BaseResponse<DeptResponse> {
		log.info(logEndpoint("get_auth_dept"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptResponse() }
		)
	}

	/**
	 * Получение списка отделов по parentId
	 */
	@PostMapping("current_list")
	private suspend fun getCurrentDepts(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetCurrentDeptsRequest
	): BaseResponse<List<DeptResponse>> {
		log.info(logEndpoint("current_list"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptsResponse() }
		)
	}

	@PostMapping("delete")
	private suspend fun delete(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: DeleteDeptRequest
	): BaseResponse<DeptDetailsResponse> {
		log.info(logEndpoint("delete"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptDetails() }
		)
	}

	@PostMapping("img_add")
	suspend fun imageAdd(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestPart("file") file: MultipartFile,
		@RequestPart("authId") authId: String,
		@RequestPart("deptId") deptId: String,
	): BaseResponse<BaseImageResponse> {
		log.info(logEndpoint("img_add"))
		log.info(logRequest(deptId))
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		val context = DeptContext().apply { command = DeptCommand.IMG_ADD }
		return baseImageProcessMem(
			authData = authData,
			context = context,
			processor = deptProcessor,
			multipartFile = file,
			authId = authId.toLongOr0(),
			entityId = deptId.toLongOr0(),
		)
	}

	@PostMapping("img_delete")
	private suspend fun imageDelete(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: DeleteDeptImageRequest
	): BaseResponse<BaseImageResponse> {
		log.info(logEndpoint("img_delete"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseImageResponse() }
		)
	}

	/**
	 * Сохранение настроек на уровне компании
	 * [deptId] - необходимо заполнить для Владельца, для определения конкретной компании.
	 *    Может быть указан любой отдел компании, бэк сам определит id компании.
	 *    Для всех остальных пользователей поле игнорируется (определяется автоматически).
	 * [payName] - Наименование валюты компании (может быть не заполнено)
	 */
	@PostMapping("save_settings")
	private suspend fun saveSettings(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: SaveDeptSettingsRequest
	): BaseResponse<DeptSettings> {
		log.info(logEndpoint("save_settings"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptSettings() }
		)
	}

	/**
	 * Получение настроек уровня компании
	 * [deptId] - необходимо заполнить для Владельца, для определения конкретной компании.
	 *    Может быть указан любой отдел компании, бэк сам определит id компании.
	 *    Для всех остальных пользователей поле игнорируется (определяется автоматически).
	 */
	@PostMapping("get_settings")
	private suspend fun getSettings(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetDeptSettingsRequest
	): BaseResponse<DeptSettings> {
		log.info(logEndpoint("get_settings"))
		log.info(logRequest(request))
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = deptProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportDeptSettings() }
		)
	}

	/**
	 * !!!! Set ADMIN role
	 */
//	@PostMapping("admin/img")
//	private suspend fun setMainImages(
//		@RequestHeader(name = AUTH) bearerToken: String,
//		@RequestBody request: SetMainDeptImagesRequest
//	): BaseResponse<Unit> {
//		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
//		return authProcess(
//			processor = deptProcessor,
//			authRequest = baseRequest,
//			fromTransport = { fromTransport(it) },
//			toTransport = { toTransportUnit() }
//		)
//	}

	companion object {
		private val log: Logger = LoggerFactory.getLogger(DeptController::class.java)
	}

}