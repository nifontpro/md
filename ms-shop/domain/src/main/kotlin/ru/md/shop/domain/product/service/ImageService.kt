package ru.md.shop.domain.product.service

import ru.md.base_domain.image.model.BaseImage

interface ImageService {
	fun addImage(productId: Long, baseImage: BaseImage): BaseImage
	fun setMainImage(productId: Long): BaseImage?
	fun deleteImage(productId: Long, imageId: Long): BaseImage
	fun addSecondImage(productId: Long, baseImage: BaseImage): BaseImage
	fun deleteSecondImage(productId: Long, imageId: Long): BaseImage
}