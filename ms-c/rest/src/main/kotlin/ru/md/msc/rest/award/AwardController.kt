package ru.md.msc.rest.award

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.msc.domain.award.biz.proc.AwardCommand
import ru.md.msc.domain.award.biz.proc.AwardContext
import ru.md.msc.domain.award.biz.proc.AwardProcessor
import ru.md.msc.domain.award.model.Activity
import ru.md.msc.domain.image.model.BaseImage
import ru.md.msc.rest.award.mappers.fromTransport
import ru.md.msc.rest.award.mappers.toTransportActivity
import ru.md.msc.rest.award.mappers.toTransportAwardDetails
import ru.md.msc.rest.award.mappers.toTransportAwards
import ru.md.msc.rest.award.model.request.*
import ru.md.msc.rest.award.model.response.AwardDetailsResponse
import ru.md.msc.rest.award.model.response.AwardResponse
import ru.md.msc.rest.base.*
import ru.md.msc.rest.base.mappers.toTransportBaseImage
import ru.md.msc.rest.utils.JwtUtils

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
		return process(
			processor = awardProcessor,
			baseRequest = baseRequest,
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
		return process(
			processor = awardProcessor,
			baseRequest = baseRequest,
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
		return process(
			processor = awardProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportAwardDetails() }
		)
	}

	@PostMapping("get_dept")
	private suspend fun getAwardByDept(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetAwardsByDeptRequest
	): BaseResponse<List<AwardResponse>> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = awardProcessor,
			baseRequest = baseRequest,
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
		return process(
			processor = awardProcessor,
			baseRequest = baseRequest,
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
		return process(
			processor = awardProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseImage() }
		)
	}

	@PostMapping("award_user")
	private suspend fun awardUser(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: AwardUserRequest
	): BaseResponse<Activity> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return process(
			processor = awardProcessor,
			baseRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportActivity() }
		)
	}

}