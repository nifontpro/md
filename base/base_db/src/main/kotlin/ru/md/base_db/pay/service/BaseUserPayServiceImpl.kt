package ru.md.base_db.pay.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.md.base_db.pay.model.UserPayEntity
import ru.md.base_db.pay.model.mappers.toUserPay
import ru.md.base_db.pay.repo.BaseUserPayRepo
import ru.md.base_domain.pay.model.UserPay
import ru.md.base_domain.pay.service.BaseUserPayService

@Service
class BaseUserPayServiceImpl(
	private val baseUserPayRepo: BaseUserPayRepo
) : BaseUserPayService {

	@Transactional
	override fun changeBalance(userId: Long, delta: Int, comment: String?) {
		val userPayEntity = baseUserPayRepo.findByUserId(userId) ?: run {
			val newUserPayEntity = UserPayEntity(userId = userId, balance = delta)
			baseUserPayRepo.save(newUserPayEntity)
			return
		}
		userPayEntity.balance += delta
	}

	override fun getPayData(userId: Long): UserPay {
		val userPayEntity = baseUserPayRepo.findByUserId(userId) ?: throw Exception()
		return userPayEntity.toUserPay()
	}

}