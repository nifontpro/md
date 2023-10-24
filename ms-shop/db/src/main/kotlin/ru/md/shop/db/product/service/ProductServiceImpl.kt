package ru.md.shop.db.product.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.mapper.toBaseImage
import ru.md.base_domain.image.model.BaseImage
import ru.md.shop.db.product.model.ProductImageEntity
import ru.md.shop.db.product.model.mappers.toProductDetails
import ru.md.shop.db.product.model.mappers.toProductDetailsEntity
import ru.md.shop.db.product.repo.ProductDetailsRepository
import ru.md.shop.db.product.repo.ProductImageRepository
import ru.md.shop.db.product.repo.ProductRepository
import ru.md.shop.domain.product.biz.proc.ProductNotFoundException
import ru.md.shop.domain.product.model.ProductDetails
import ru.md.shop.domain.product.service.ProductService
import java.time.LocalDateTime

@Service
class ProductServiceImpl(
	private val productRepository: ProductRepository,
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
			productEntity.price = productDetails.product.price
			description = productDetails.description
			siteUrl = productDetails.siteUrl
		}

		return oldProductDetailsEntity.toProductDetails()
	}

	@Transactional
	override fun deleteById(productId: Long) {
		productRepository.deleteById(productId)
	}

	override fun findProductDetailsById(productId: Long): ProductDetails {
		val productDetailsEntity = productDetailsRepository.findByIdOrNull(productId) ?: throw ProductNotFoundException()
		return productDetailsEntity.toProductDetails()
	}

	override fun findDeptIdByProductId(productId: Long): Long {
		return productRepository.findDeptId(productId) ?: throw ProductNotFoundException()
	}

	@Transactional
	override fun addImage(productId: Long, baseImage: BaseImage): BaseImage {
		val productImageEntity = ProductImageEntity(
			productId = productId,
			normalUrl = baseImage.normalUrl,
			normalKey = baseImage.normalKey,
			miniUrl = baseImage.miniUrl ?: "",
			miniKey = baseImage.miniKey ?: "",
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
		val images = productDetailsEntity.images

		var productImageEntity = images.firstOrNull() ?: run {
			productEntity.mainImg = null
			return null
		}

		images.forEach {
			if (it.createdAt > productImageEntity.createdAt) {
				productImageEntity = it
			} else if (it.main) {
				it.main = false
			}
		}

		productImageEntity.main = true
		productEntity.mainImg = productImageEntity.miniUrl
		return productImageEntity.toBaseImage()
	}

	@Transactional
	override fun deleteImage(productId: Long, imageId: Long): BaseImage {
		val productImageEntity = productImageRepository.findByIdAndProductId(productId = productId, imageId = imageId) ?: run {
//			throw ImageNotFoundException()
			throw Exception()
		}
		productImageRepository.delete(productImageEntity)
		return productImageEntity.toBaseImage()
	}
}