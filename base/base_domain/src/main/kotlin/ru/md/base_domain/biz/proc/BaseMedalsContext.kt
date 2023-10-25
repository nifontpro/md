package ru.md.base_domain.biz.proc

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.user.model.User

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
//	lateinit var s3Repository: S3Repository


}