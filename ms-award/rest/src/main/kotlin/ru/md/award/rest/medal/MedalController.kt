package ru.md.award.rest.medal

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.md.award.domain.medal.biz.proc.MedalCommand
import ru.md.award.domain.medal.biz.proc.MedalContext
import ru.md.award.domain.medal.biz.proc.MedalProcessor
import ru.md.award.rest.medal.mappers.fromTransport
import ru.md.award.rest.medal.mappers.toTransportMedalDetails
import ru.md.award.rest.medal.model.request.*
import ru.md.award.rest.medal.model.response.MedalDetailsResponse
import ru.md.base_domain.model.BaseResponse
import ru.md.base_rest.base.authProcess
import ru.md.base_rest.base.toLongOr0
import ru.md.base_rest.image.baseImageProcessMem
import ru.md.base_rest.model.mapper.toTransportBaseImageResponse
import ru.md.base_rest.model.request.AUTH
import ru.md.base_rest.model.response.BaseImageResponse
import ru.md.base_rest.utils.JwtUtils
import ru.md.base_rest.utils.testResponse

@RestController
@RequestMapping("medal")
class MedalController(
	private val medalProcessor: MedalProcessor,
	private val jwtUtils: JwtUtils,
) {

	@GetMapping("test")
	suspend fun test() = testResponse()

	@PostMapping("create")
	private suspend fun create(
		@RequestBody request: CreateMedalRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<MedalDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = medalProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportMedalDetails() }
		)
	}

	@PostMapping("get_id")
	private suspend fun getById(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: GetMedalByIdRequest
	): BaseResponse<MedalDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = medalProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportMedalDetails() }
		)
	}

	@PostMapping("update")
	private suspend fun update(
		@RequestBody request: UpdateMedalRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<MedalDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = medalProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportMedalDetails() }
		)
	}

	@PostMapping("delete")
	private suspend fun update(
		@RequestBody request: DeleteMedalRequest,
		@RequestHeader(name = AUTH) bearerToken: String
	): BaseResponse<MedalDetailsResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = medalProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportMedalDetails() }
		)
	}

	@PostMapping("img_add")
	suspend fun imageAdd(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestPart("file") file: MultipartFile,
		@RequestPart("authId") authId: String,
		@RequestPart("medalId") medalId: String,
	): BaseResponse<BaseImageResponse> {
		val authData = jwtUtils.decodeBearerJwt(bearerToken = bearerToken)
		val context = MedalContext().apply { command = MedalCommand.IMG_ADD }
		return baseImageProcessMem(
			authData = authData,
			context = context,
			processor = medalProcessor,
			multipartFile = file,
			authId = authId.toLongOr0(),
			entityId = medalId.toLongOr0(),
		)
	}

	@PostMapping("img_delete")
	private suspend fun imageDelete(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: DeleteMedalImageRequest
	): BaseResponse<BaseImageResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = medalProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseImageResponse() }
		)
	}

	@PostMapping("img_gal")
	private suspend fun imageAddFromGallery(
		@RequestHeader(name = AUTH) bearerToken: String,
		@RequestBody request: AddMedalImageFromGalleryRequest
	): BaseResponse<BaseImageResponse> {
		val baseRequest = jwtUtils.baseRequest(request, bearerToken)
		return authProcess(
			processor = medalProcessor,
			authRequest = baseRequest,
			fromTransport = { fromTransport(it) },
			toTransport = { toTransportBaseImageResponse() }
		)
	}

}