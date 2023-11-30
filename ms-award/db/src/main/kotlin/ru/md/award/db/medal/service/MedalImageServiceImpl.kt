package ru.md.award.db.medal.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.award.db.medal.model.MedalImageEntity
import ru.md.award.db.medal.repo.MedalDetailsRepository
import ru.md.award.db.medal.repo.MedalImageRepository
import ru.md.award.domain.medal.biz.proc.MedalNotFoundException
import ru.md.award.domain.medal.service.MedalImageService
import ru.md.base_db.image.mappers.toBaseImage
import ru.md.base_domain.errors.ImageNotFoundException
import ru.md.base_domain.gallery.SmallItem
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.image.model.ImageType
import ru.md.base_domain.s3.repo.BaseS3Repository
import java.time.LocalDateTime

@Service
class MedalImageServiceImpl(
	private val medalDetailsRepository: MedalDetailsRepository,
	private val medalImageRepository: MedalImageRepository,
	private val baseS3Repository: BaseS3Repository
) : MedalImageService {

	@Transactional
	override fun addImage(medalId: Long, baseImage: BaseImage): BaseImage {
		val medalImageEntity = MedalImageEntity(
			medalId = medalId,
			originUrl = baseImage.originUrl,
			originKey = baseImage.originKey,
			normalUrl = baseImage.normalUrl,
			normalKey = baseImage.normalKey,
			miniUrl = baseImage.miniUrl,
			miniKey = baseImage.miniKey,
			type = baseImage.type,
			createdAt = LocalDateTime.now()
		)
		medalImageRepository.save(medalImageEntity)
		return medalImageEntity.toBaseImage()
	}

	@Transactional
	override fun setMainImage(medalId: Long): BaseImage? {
		val medalDetailsEntity = medalDetailsRepository.findByIdOrNull(medalId) ?: throw MedalNotFoundException()
		val medalEntity = medalDetailsEntity.medalEntity

		var medalImageEntity = medalDetailsEntity.images.firstOrNull() ?: run {
			medalEntity.mainImg = null
			medalEntity.normImg = null
			return null
		}

		medalDetailsEntity.images.forEach {
			if (it.createdAt > medalImageEntity.createdAt) {
				medalImageEntity = it
			} else if (it.main) {
				it.main = false
			}
		}

		medalImageEntity.main = true
		medalEntity.mainImg = if (medalImageEntity.miniUrl != null) {
			medalImageEntity.miniUrl
		} else {
			medalImageEntity.normalUrl
		}

		medalEntity.normImg = medalImageEntity.normalUrl
		return medalImageEntity.toBaseImage()
	}

	@Transactional
	override fun deleteImage(medalId: Long, imageId: Long): BaseImage  {
		val awardImageEntity = medalImageRepository.findByIdAndMedalId(medalId = medalId, imageId = imageId) ?: run {
			throw ImageNotFoundException()
		}
		medalImageRepository.delete(awardImageEntity)
		return awardImageEntity.toBaseImage()
	}

	override fun addGalleryImage(medalId: Long, smallItem: SmallItem): BaseImage {
		val medalImageEntity = MedalImageEntity(
			medalId = medalId,
			originUrl = smallItem.originUrl,
			normalUrl = smallItem.normalUrl,
			miniUrl = smallItem.miniUrl,
			type = ImageType.SYSTEM,
			createdAt = LocalDateTime.now()
		)
		medalImageRepository.save(medalImageEntity)
		return medalImageEntity.toBaseImage()
	}

}