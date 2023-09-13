package ru.md.msc.domain.base.biz

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.md.base_domain.biz.proc.BaseContext
import ru.md.base_domain.image.model.BaseImage
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.msg.model.UserMsg
import ru.md.msc.domain.msg.service.MessageService
import ru.md.msc.domain.s3.repository.S3Repository
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.service.UserService

abstract class BaseClientContext(
	var authUser: User = User(),
	var isAuthUserHasAdminRole: Boolean = false,

	var authId: Long = 0,
	var userId: Long = 0,
	var deptId: Long = 0,
	var awardId: Long = 0,
	var rootDeptId: Long = 0,

	var modifyUser: User = User(),
	var isModifyUserHasAdminRole: Boolean = false,

	var imageId: Long = 0,
	var baseImage: BaseImage = BaseImage(),
	var baseImages: List<BaseImage> = emptyList(),
	var deleteImageOnFailing: Boolean = false,

	var userMsg: UserMsg = UserMsg(),
	var prefixUrl: String = "",

	var isAuth: Boolean = false,

	var deleteForever: Boolean = false

	) : BaseContext() {

	lateinit var userService: UserService
	lateinit var deptService: DeptService
	lateinit var s3Repository: S3Repository
	lateinit var messageService: MessageService

	val log: Logger = LoggerFactory.getLogger(BaseClientContext::class.java)
}