package ru.md.shop.db.pay.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.base.mapper.toPageRequest
import ru.md.base_db.base.mapper.toPageResult
import ru.md.base_db.base.mapper.toSearchUpperOrNull
import ru.md.base_db.pay.model.mappers.toUserPay
import ru.md.base_db.pay.repo.BaseUserPayRepo
import ru.md.base_db.user.model.UserEntity
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.base_domain.pay.model.UserPay
import ru.md.shop.db.base.repo.BaseProductRepo
import ru.md.shop.db.pay.model.PayDataEntity
import ru.md.shop.db.pay.model.mappers.toPayData
import ru.md.shop.db.pay.model.mappers.toPayDataEntity
import ru.md.shop.db.pay.model.mappers.toPayDataWithUserDept
import ru.md.shop.db.pay.repo.PayDataRepo
import ru.md.shop.domain.pay.biz.proc.InsufficientUserBalanceException
import ru.md.shop.domain.pay.biz.proc.PayDataNotFoundException
import ru.md.shop.domain.pay.biz.proc.UserPayNotFoundException
import ru.md.shop.domain.pay.model.PayCode
import ru.md.shop.domain.pay.model.PayData
import ru.md.shop.domain.pay.service.PayService
import ru.md.shop.domain.product.biz.proc.InsufficientProductQuantityException
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
	override fun payProduct(productId: Long, userId: Long): PayData {
		val productEntity = baseProductRepo.findByIdOrNull(productId) ?: throw ProductNotFoundException()
		if (productEntity.count < 1) throw InsufficientProductQuantityException()
//		val userPayEntity = baseUserPayRepo.findByUserId(userId) ?: throw UserPayNotFoundException()
		val userPayEntity = baseUserPayRepo.findByUserId(userId) ?: throw InsufficientUserBalanceException()
		val price = productEntity.price

		if (userPayEntity.balance < price) throw InsufficientUserBalanceException()

		val payDataEntity = PayDataEntity(
			dateOp = LocalDateTime.now(),
			userEntity = UserEntity(id = userId),
			productEntity = productEntity,
			price = -price,
			payCode = PayCode.PAY,
			isActive = true,
			isBalance = true
		)

		payDataRepo.save(payDataEntity)

		userPayEntity.balance -= price
		productEntity.count -= 1
		return payDataEntity.toPayData()
	}

	override fun getPaysData(
		deptId: Long,
		userId: Long,
		baseQuery: BaseQuery,
		payCode: PayCode,
		isActive: Boolean?
	): PageResult<PayData> {
		return payDataRepo.findPaysDataByCompany(
			deptId = deptId,
			userId = userId,
			isActive = isActive,
			payCode = payCode.toSearch(),
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
			filter = baseQuery.filter.toSearchUpperOrNull(),
			pageable = baseQuery.toPageRequest()
		).toPageResult { it.toPayDataWithUserDept() }
	}

	override fun findById(payDataId: Long): PayData {
		val payDataEntity = payDataRepo.findByIdWithUserDept(payDataId) ?: throw PayDataNotFoundException()
		return payDataEntity.toPayDataWithUserDept()
	}

	@Transactional
	override fun giveProduct(payData: PayData): PayData {
		val payDataEntity = payData.toPayDataEntity()
		val newPayDataEntity = PayDataEntity(
			dateOp = LocalDateTime.now(),
			userEntity = payDataEntity.userEntity,
			productEntity = payDataEntity.productEntity,
			price = payData.price,
			payCode = PayCode.GIVEN,
			isActive = true,
			isBalance = false
		)
		payDataEntity.isActive = false
		payDataRepo.save(payDataEntity)
		payDataRepo.save(newPayDataEntity)
		return newPayDataEntity.toPayDataWithUserDept()
	}

	@Transactional
	override fun returnProduct(payData: PayData): PayData {
		val payDataEntity = payData.toPayDataEntity()
		val newPayDataEntity = PayDataEntity(
			dateOp = LocalDateTime.now(),
			userEntity = payDataEntity.userEntity,
			productEntity = payDataEntity.productEntity,
			price = -payData.price,
			payCode = PayCode.RETURN,
			isActive = true,
			isBalance = true
		)
		payDataRepo.save(newPayDataEntity)

		val userPayEntity = baseUserPayRepo.findByUserId(payData.user.id) ?: throw UserPayNotFoundException()
		userPayEntity.balance += -payData.price

		payDataEntity.isActive = false
		payDataEntity.productEntity?.let { product ->
			product.count += 1
			baseProductRepo.save(product)
		} ?: throw ProductNotFoundException()
		payDataRepo.save(payDataEntity)

		return newPayDataEntity.toPayDataWithUserDept()
	}

}