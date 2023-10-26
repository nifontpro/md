package ru.md.base_domain.biz.proc

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.s3.repo.BaseS3Repository
import ru.md.base_domain.user.model.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class BaseMedalsContext(
	var authUser: User = User(),
	var user: User = User(),
	var isAuthUserHasAdminRole: Boolean = false,

	var authId: Long = 0,
	var userId: Long = 0,
	var deptId: Long = 0,
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
	lateinit var baseS3Repository: BaseS3Repository

	open val log: Logger = LoggerFactory.getLogger(BaseMedalsContext::class.java)
}