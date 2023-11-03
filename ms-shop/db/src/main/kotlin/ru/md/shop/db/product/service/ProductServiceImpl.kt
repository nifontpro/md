package ru.md.shop.db.product.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.base.mapper.toPageRequest
import ru.md.base_db.base.mapper.toPageResult
import ru.md.base_db.base.mapper.toSearchUpperOrNull
import ru.md.base_db.image.mappers.toBaseImage
import ru.md.base_domain.errors.ImageNotFoundException
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.shop.db.product.model.ProductImageEntity
import ru.md.shop.db.product.model.mappers.toProduct
import ru.md.shop.db.product.model.mappers.toProductDetails
import ru.md.shop.db.product.model.mappers.toProductDetailsEntity
import ru.md.shop.db.product.repo.ProductDetailsRepository
import ru.md.shop.db.product.repo.ProductImageRepository
import ru.md.shop.db.product.repo.ProductRepo
import ru.md.shop.domain.product.biz.proc.ProductNotFoundException
import ru.md.shop.domain.product.model.Product
import ru.md.shop.domain.product.model.ProductDetails
import ru.md.shop.domain.product.service.ProductService
import java.time.LocalDateTime

@Service
class ProductServiceImpl(
	private val productRepo: ProductRepo,
	private val productDetailsRepository: ProductDetailsRepository,
	private val productImageRepository: ProductImageRepository,
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

	@Transactional
	override fun deleteById(productId: Long) {
		productRepo.deleteById(productId)
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

	@Transactional
	override fun addImage(productId: Long, baseImage: BaseImage): BaseImage {
		val productImageEntity = ProductImageEntity(
			productId = productId,
			originUrl = baseImage.originUrl,
			originKey = baseImage.originKey,
			normalUrl = baseImage.normalUrl,
			normalKey = baseImage.normalKey,
			miniUrl = baseImage.miniUrl,
			miniKey = baseImage.miniKey,
			type = baseImage.type,
			createdAt = LocalDateTime.now()
		)
		productImageRepository.save(productImageEntity)
		return productImageEntity.toBaseImage()
	}

	@Transactional
	override fun setMainImage(productId: Long): BaseImage? {
		val productDetailsEntity = productDetailsRepository.findByIdOrNull(productId) ?: throw ProductNotFoundException()
		val productEntity = productDetailsEntity.productEntity

		var productImageEntity = productDetailsEntity.images.firstOrNull() ?: run {
			productEntity.mainImg = null
			productEntity.normImg = null
			return null
		}

		productDetailsEntity.images.forEach {
			if (it.createdAt > productImageEntity.createdAt) {
				productImageEntity = it
			} else if (it.main) {
				it.main = false
			}
		}

		productImageEntity.main = true
		productEntity.mainImg = productImageEntity.miniUrl
		productEntity.normImg = productImageEntity.normalUrl
		return productImageEntity.toBaseImage()
	}

	@Transactional
	override fun deleteImage(productId: Long, imageId: Long): BaseImage {
		val productImageEntity =
			productImageRepository.findByIdAndProductId(productId = productId, imageId = imageId) ?: run {
				throw ImageNotFoundException()
			}
		productImageRepository.delete(productImageEntity)
		return productImageEntity.toBaseImage()
	}
}