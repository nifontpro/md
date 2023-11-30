package ru.md.award.domain.medal.service

import ru.md.base_domain.gallery.SmallItem
import ru.md.base_domain.image.model.BaseImage

interface MedalImageService {
	fun addImage(medalId: Long, baseImage: BaseImage): BaseImage
	fun setMainImage(medalId: Long): BaseImage?
	fun deleteImage(medalId: Long, imageId: Long): BaseImage
	fun addGalleryImage(medalId: Long, smallItem: SmallItem): BaseImage
}