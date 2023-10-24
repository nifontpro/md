package ru.md.shop.domain.base.biz

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.md.base_domain.biz.proc.BaseContext
import ru.md.base_domain.image.model.BaseImage

abstract class BaseShopContext(
	var authId: Long = 0,
	var userId: Long = 0,
	var deptId: Long = 0,
	var productId: Long = 0,
	var rootDeptId: Long = 0,

	var imageId: Long = 0,
	var baseImage: BaseImage = BaseImage(),
	var baseImages: List<BaseImage> = emptyList(),
	var deleteImageOnFailing: Boolean = false,

	var prefixUrl: String = "",

	var isAuth: Boolean = false,

	) : BaseContext() {

//	lateinit var userService: UserService
//	lateinit var deptService: DeptService
//	lateinit var s3Repository: S3Repository
//	lateinit var messageService: MessageService
//	lateinit var emailService: EmailService

	val log: Logger = LoggerFactory.getLogger(BaseShopContext::class.java)
}