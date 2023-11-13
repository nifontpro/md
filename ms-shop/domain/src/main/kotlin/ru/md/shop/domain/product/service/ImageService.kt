package ru.md.shop.domain.product.service

import ru.md.base_domain.image.model.BaseImage

interface ImageService {
	fun addImage(productId: Long, baseImage: BaseImage): BaseImage
	fun setMainImage(productId: Long): BaseImage?
	suspend fun deleteImage(productId: Long, imageId: Long): BaseImage
	fun addSecondImage(productId: Long, baseImage: BaseImage): BaseImage
	suspend fun deleteSecondImage(productId: Long, imageId: Long): BaseImage
}