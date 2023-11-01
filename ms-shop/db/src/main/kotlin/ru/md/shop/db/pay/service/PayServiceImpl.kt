package ru.md.shop.db.pay.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.pay.model.mappers.toUserPay
import ru.md.base_db.pay.repo.BaseUserPayRepo
import ru.md.base_db.user.model.UserEntity
import ru.md.base_domain.pay.model.UserPay
import ru.md.shop.db.base.repo.BaseProductRepo
import ru.md.shop.db.pay.model.PayDataEntity
import ru.md.shop.db.pay.repo.PayDataRepo
import ru.md.shop.domain.pay.biz.proc.InsufficientUserBalanceException
import ru.md.shop.domain.pay.biz.proc.UserPayNotFoundException
import ru.md.shop.domain.pay.model.PayCode
import ru.md.shop.domain.pay.service.PayService
import ru.md.shop.domain.product.biz.proc.ProductNotFoundException
import java.time.LocalDateTime

@Service
class PayServiceImpl(
//	private val userPayRepo: UserPayRepo,
	private val baseUserPayRepo: BaseUserPayRepo,
	private val baseProductRepo: BaseProductRepo,
	private val payDataRepo: PayDataRepo,
) : PayService {

	override fun getUserPayData(userId: Long): UserPay {
		val userPayEntity = baseUserPayRepo.findByUserId(userId) ?: throw UserPayNotFoundException()
		return userPayEntity.toUserPay()
	}

	@Transactional
	override fun buyProduct(productId: Long, userId: Long): UserPay {
		val productEntity = baseProductRepo.findByIdOrNull(productId) ?: throw ProductNotFoundException()
		val userPayEntity = baseUserPayRepo.findByUserId(userId) ?: throw UserPayNotFoundException()
		val price = productEntity.price

		if (userPayEntity.balance < price) throw InsufficientUserBalanceException()

		val payDataEntity = PayDataEntity(
			dateOp = LocalDateTime.now(),
			userEntity = UserEntity(id = userId),
			productEntity = productEntity,
			price = price,
			payCode = PayCode.PAY,
			isActive = true
		)

		payDataRepo.save(payDataEntity)

		userPayEntity.balance -= price
		return userPayEntity.toUserPay()
	}

}