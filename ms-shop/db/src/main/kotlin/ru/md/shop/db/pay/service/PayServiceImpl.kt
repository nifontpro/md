package ru.md.shop.db.pay.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.pay.model.mappers.toUserPay
import ru.md.base_db.pay.repo.BaseUserPayRepo
import ru.md.base_domain.pay.model.UserPay
import ru.md.shop.db.pay.model.TrashEntity
import ru.md.shop.db.pay.model.mappers.toTrash
import ru.md.shop.db.pay.repo.TrashRepo
import ru.md.shop.db.product.repo.ProductRepo
import ru.md.shop.domain.pay.biz.proc.UserPayNotFoundException
import ru.md.shop.domain.pay.model.Trash
import ru.md.shop.domain.pay.service.PayService
import ru.md.shop.domain.product.biz.proc.InsufficientProductQuantityException
import ru.md.shop.domain.product.biz.proc.ProductNotFoundException
import java.time.LocalDateTime

@Service
class PayServiceImpl(
//	private val userPayRepo: UserPayRepo,
	private val baseUserPayRepo: BaseUserPayRepo,
	private val productRepo: ProductRepo,
	private val trashRepo: TrashRepo,
) : PayService {

	override fun getUserPayData(userId: Long): UserPay {
		val userPayEntity = baseUserPayRepo.findByUserId(userId) ?: throw UserPayNotFoundException()
		return userPayEntity.toUserPay()
	}

	@Transactional
	fun addProductToTrash(userId: Long, productId: Long, count: Int): Trash {
		val productEntity = productRepo.findByIdOrNull(productId) ?: throw ProductNotFoundException()
		if (count > productEntity.count) throw InsufficientProductQuantityException()
		productEntity.count -= count
		val trashEntity = TrashEntity(
			userId = userId,
			productId = productId,
			quantity = count,
			addAt = LocalDateTime.now()
		)
		trashRepo.save(trashEntity)
		return trashEntity.toTrash()
	}

}