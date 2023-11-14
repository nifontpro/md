package ru.md.shop.db.product.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import ru.md.base_db.base.mapper.toPageRequest
import ru.md.base_db.base.mapper.toPageResult
import ru.md.base_db.base.mapper.toSearchUpperOrNull
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.base_domain.s3.repo.BaseS3Repository
import ru.md.shop.db.product.model.mappers.toProduct
import ru.md.shop.db.product.model.mappers.toProductDetails
import ru.md.shop.db.product.model.mappers.toProductDetailsEntity
import ru.md.shop.db.product.repo.ProductDetailsRepository
import ru.md.shop.db.product.repo.ProductRepo
import ru.md.shop.domain.product.biz.proc.ProductNotFoundException
import ru.md.shop.domain.product.model.Product
import ru.md.shop.domain.product.model.ProductDetails
import ru.md.shop.domain.product.service.ProductService

@Service
class ProductServiceImpl(
	private val productRepo: ProductRepo,
	private val productDetailsRepository: ProductDetailsRepository,
	private val baseS3Repository: BaseS3Repository,
	private val transactionManager: PlatformTransactionManager,
) : ProductService {

	@Transactional
	override fun create(productDetails: ProductDetails): ProductDetails {
		val productDetailsEntity = productDetails.toProductDetailsEntity()
		productDetailsRepository.save(productDetailsEntity)
		return productDetailsEntity.toProductDetails()
	}

	@Transactional
	override fun update(productDetails: ProductDetails): ProductDetails {
		val oldProductDetailsEntity = productDetailsRepository.findByIdOrNull(productDetails.product.id) ?: run {
			throw ProductNotFoundException()
		}

		with(oldProductDetailsEntity) {
			productEntity.name = productDetails.product.name
			productEntity.shortDescription = productDetails.product.shortDescription
			productEntity.price = productDetails.product.price
			productEntity.count = productDetails.product.count
			description = productDetails.description
			place = productDetails.place
			siteUrl = productDetails.siteUrl
		}

		return oldProductDetailsEntity.toProductDetails()
	}

	override suspend fun deleteById(productId: Long): ProductDetails {
		val productDetailsEntity = productDetailsRepository.findByIdOrNull(productId) ?: run {
			throw ProductNotFoundException()
		}

		val transaction = transactionManager.getTransaction(transactionDefinition)
		productRepo.deleteById(productId)
		baseS3Repository.deleteImages(productDetailsEntity.images)
		baseS3Repository.deleteImages(productDetailsEntity.secondImages)
		transactionManager.commit(transaction)

		return productDetailsEntity.toProductDetails()
	}

	override fun findProductDetailsById(productId: Long): ProductDetails {
		val productDetailsEntity = productDetailsRepository.findByIdOrNull(productId) ?: throw ProductNotFoundException()
		return productDetailsEntity.toProductDetails()
	}

	override fun findByDeptId(
		deptId: Long,
		maxPrice: Int?,
		available: Boolean,
		baseQuery: BaseQuery
	): PageResult<Product> {
		val res = productRepo.findByCompany(
			deptId = deptId,
			maxPrice = maxPrice,
			available = available,
			filter = baseQuery.filter.toSearchUpperOrNull(),
			pageable = baseQuery.toPageRequest()
		)
		return res.toPageResult { it.toProduct() }
	}

	companion object {
		val transactionDefinition = DefaultTransactionDefinition()
	}

}