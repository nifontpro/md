package ru.md.shop.db.base.service

import org.springframework.stereotype.Service
import ru.md.shop.db.base.repo.BaseProductRepo
import ru.md.shop.domain.base.service.BaseProductService
import ru.md.shop.domain.product.biz.proc.ProductNotFoundException

@Service
class BaseProductServiceImpl(
	private val baseProductRepo: BaseProductRepo
) : BaseProductService {

	override fun findDeptIdByProductId(productId: Long): Long {
		return baseProductRepo.findDeptId(productId) ?: throw ProductNotFoundException()
	}

}