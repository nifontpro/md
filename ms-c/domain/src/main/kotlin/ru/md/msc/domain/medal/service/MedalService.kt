package ru.md.msc.domain.medal.service

import ru.md.base_domain.gallery.SmallItem
import ru.md.base_domain.image.model.BaseImage
import ru.md.msc.domain.medal.model.Medal
import ru.md.msc.domain.medal.model.MedalDetails

interface MedalService {
	fun create(medalDetails: MedalDetails): MedalDetails
	fun update(medalDetails: MedalDetails): MedalDetails
	fun findMedalById(medalId: Long): Medal
	fun findMedalDetailsById(medalId: Long): MedalDetails
	fun deleteById(medalId: Long)
	fun findDeptIdByMedalId(medalId: Long): Long
	fun addImage(medalId: Long, baseImage: BaseImage): BaseImage
	fun setMainImage(medalId: Long): BaseImage?
	fun deleteImage(medalId: Long, imageId: Long): BaseImage
	fun addGalleryImage(medalId: Long, smallItem: SmallItem): BaseImage
}