package ru.md.msc.domain.base.biz

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.md.base_domain.biz.proc.BaseContext
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.image.repository.S3Repository
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.service.UserService

abstract class BaseClientContext(
	var authUser: User = User(),
	var isAuthUserHasAdminRole: Boolean = false,

	var userId: Long = 0,
	var deptId: Long = 0,

) : BaseContext() {

	lateinit var userService: UserService
	lateinit var deptService: DeptService
	lateinit var s3Repository: S3Repository

	val log: Logger = LoggerFactory.getLogger(BaseClientContext::class.java)
}