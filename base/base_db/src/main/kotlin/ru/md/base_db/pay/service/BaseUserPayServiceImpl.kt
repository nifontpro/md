package ru.md.base_db.pay.service

import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.md.base_db.pay.model.UserPayEntity
import ru.md.base_db.pay.repo.BaseUserPayRepo
import ru.md.base_domain.pay.service.BaseUserPayService

@Service
class BaseUserPayServiceImpl(
	private val baseUserPayRepo: BaseUserPayRepo
) : BaseUserPayService {

	@Transactional
	override fun changeBalance(userId: Long, delta: Int, comment: String?): Int {
		val userPayEntity = baseUserPayRepo.findByUserId(userId) ?: run {
			val newUserPayEntity = UserPayEntity(userId = userId, balance = delta)
			baseUserPayRepo.save(newUserPayEntity)
			return delta
		}
		userPayEntity.balance += delta
		log.info("Пополнение баланса пользователя $userId на $delta, остаток: ${userPayEntity.balance}")
		return userPayEntity.balance
	}

	companion object {
		val log: Logger = LoggerFactory.getLogger(BaseUserPayService::class.java)
	}

}