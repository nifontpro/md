package ru.md.shop.db.product.service

import jakarta.transaction.Transactional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.image.mappers.toBaseImage
import ru.md.base_domain.errors.ImageNotFoundException
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.s3.repo.BaseS3Repository
import ru.md.shop.db.product.model.ProductImageEntity
import ru.md.shop.db.product.model.SecondImageEntity
import ru.md.shop.db.product.repo.ProductDetailsRepository
import ru.md.shop.db.product.repo.ProductImageRepository
import ru.md.shop.db.product.repo.SecondImageRepository
import ru.md.shop.domain.product.biz.proc.ProductNotFoundException
import ru.md.shop.domain.product.service.ImageService
import java.time.LocalDateTime

@Service
class ImageServiceImpl(
	private val productDetailsRepository: ProductDetailsRepository,
	private val productImageRepository: ProductImageRepository,
	private val secondImageRepository: SecondImageRepository,
	private val baseS3Repository: BaseS3Repository
) : ImageService {

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
		productEntity.mainImg = if (productImageEntity.miniUrl != null) {
			productImageEntity.miniUrl
		} else {
			productImageEntity.normalUrl
		}
		productEntity.normImg = productImageEntity.normalUrl
		return productImageEntity.toBaseImage()
	}

	@Transactional
	override suspend fun deleteImage(productId: Long, imageId: Long): BaseImage = withContext(Dispatchers.IO) {
		val productImageEntity = productImageRepository
			.findByIdAndProductId(productId = productId, imageId = imageId) ?: run {
			throw ImageNotFoundException()
		}
		productImageRepository.delete(productImageEntity)
		val baseImage = productImageEntity.toBaseImage()
		baseS3Repository.deleteBaseImage(baseImage)
		baseImage
	}

	@Transactional
	override fun addSecondImage(productId: Long, baseImage: BaseImage): BaseImage {
		val secondImageEntity = SecondImageEntity(
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
		secondImageRepository.save(secondImageEntity)
		return secondImageEntity.toBaseImage()
	}

	@Transactional
	override suspend fun deleteSecondImage(productId: Long, imageId: Long): BaseImage {
		return withContext(Dispatchers.IO) {
			val secondImageEntity =
				secondImageRepository.findByIdAndProductId(productId = productId, imageId = imageId) ?: run {
					throw ImageNotFoundException()
				}
			secondImageRepository.delete(secondImageEntity)
			val baseImage = secondImageEntity.toBaseImage()
			baseS3Repository.deleteBaseImage(baseImage)
			baseImage
		}
	}
}