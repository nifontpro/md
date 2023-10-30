package ru.md.shop.db.pay.service

import org.springframework.stereotype.Service
import ru.md.base_db.pay.model.mappers.toUserPay
import ru.md.base_db.pay.repo.BaseUserPayRepo
import ru.md.base_domain.pay.model.UserPay
import ru.md.shop.domain.pay.biz.proc.UserPayNotFoundException
import ru.md.shop.domain.pay.service.UserPayService

@Service
class UserPayServiceImpl(
//	private val userPayRepo: UserPayRepo,
	private val baseUserPayRepo: BaseUserPayRepo,
) : UserPayService {

	override fun getPayData(userId: Long): UserPay {
		val userPayEntity = baseUserPayRepo.findByUserId(userId) ?: throw UserPayNotFoundException()
		return userPayEntity.toUserPay()
	}

}