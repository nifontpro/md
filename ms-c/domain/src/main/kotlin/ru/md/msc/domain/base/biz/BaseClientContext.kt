package ru.md.msc.domain.base.biz

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.md.base_domain.biz.proc.BaseMedalsContext
import ru.md.base_domain.gallery.SmallItem
import ru.md.base_domain.user.model.User
import ru.md.msc.domain.dept.service.DeptService
import ru.md.msc.domain.email.EmailService
import ru.md.msc.domain.msg.model.UserMsg
import ru.md.msc.domain.msg.service.MessageService
import ru.md.msc.domain.s3.repository.S3Repository
import ru.md.msc.domain.user.service.UserService

abstract class BaseClientContext(
	var awardId: Long = 0,
	var medalId: Long = 0,

	var modifyUser: User = User(),
	var isModifyUserHasAdminRole: Boolean = false,

	var smallItem: SmallItem = SmallItem(), // убрать

	var userMsg: UserMsg = UserMsg(),
	var deleteForever: Boolean = false

	) : BaseMedalsContext() {

	lateinit var userService: UserService
	lateinit var deptService: DeptService
	lateinit var s3Repository: S3Repository
	lateinit var messageService: MessageService
	lateinit var emailService: EmailService

	val log: Logger = LoggerFactory.getLogger(BaseClientContext::class.java)
}