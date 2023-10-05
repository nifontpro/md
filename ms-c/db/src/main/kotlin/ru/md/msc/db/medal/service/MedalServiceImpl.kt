package ru.md.msc.db.medal.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.mapper.toBaseImage
import ru.md.base_domain.gallery.SmallItem
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.image.model.ImageType
import ru.md.msc.db.medal.model.MedalImageEntity
import ru.md.msc.db.medal.model.mapper.toMedal
import ru.md.msc.db.medal.model.mapper.toMedalDetails
import ru.md.msc.db.medal.model.mapper.toMedalDetailsEntity
import ru.md.msc.db.medal.repo.MedalDetailsRepository
import ru.md.msc.db.medal.repo.MedalImageRepository
import ru.md.msc.db.medal.repo.MedalRepository
import ru.md.msc.domain.base.biz.ImageNotFoundException
import ru.md.msc.domain.medal.biz.proc.MedalNotFoundException
import ru.md.msc.domain.medal.model.Medal
import ru.md.msc.domain.medal.model.MedalDetails
import ru.md.msc.domain.medal.service.MedalService
import java.time.LocalDateTime

@Service
class MedalServiceImpl(
	private val medalRepository: MedalRepository,
	private val medalDetailsRepository: MedalDetailsRepository,
	private val medalImageRepository: MedalImageRepository,
//	private val actRepository: ActRepository,
//	private val deptUtil: DeptUtil,
) : MedalService {

	@Transactional
	override fun create(medalDetails: MedalDetails): MedalDetails {
		val medalDetailsEntity = medalDetails.toMedalDetailsEntity()
		medalDetailsRepository.save(medalDetailsEntity)
		return medalDetailsEntity.toMedalDetails()
	}

	@Transactional
	override fun update(medalDetails: MedalDetails): MedalDetails {
		val oldMedalDetailsEntity = medalDetailsRepository.findByIdOrNull(medalDetails.medal.id) ?: run {
			throw MedalNotFoundException()
		}

		with(oldMedalDetailsEntity) {
			medalEntity.name = medalDetails.medal.name
			medalEntity.score = medalDetails.medal.score
			description = medalDetails.description
		}

		return oldMedalDetailsEntity.toMedalDetails()
	}

	@Transactional
	override fun deleteById(medalId: Long) {
		medalRepository.deleteById(medalId)
	}

	override fun findMedalById(medalId: Long): Medal {
		val medalEntity = medalRepository.findByIdOrNull(medalId) ?: throw MedalNotFoundException()
		return medalEntity.toMedal()
	}

	override fun findMedalDetailsById(medalId: Long): MedalDetails {
		val medalDetailsEntity = medalDetailsRepository.findByIdOrNull(medalId) ?: throw MedalNotFoundException()
		return medalDetailsEntity.toMedalDetails()
	}

	override fun findDeptIdByMedalId(medalId: Long): Long {
		return medalRepository.findDeptId(medalId = medalId) ?: throw MedalNotFoundException()
	}

	@Transactional
	override fun addImage(medalId: Long, baseImage: BaseImage): BaseImage {
		val medalImageEntity = MedalImageEntity(
			medalId = medalId,
			normalUrl = baseImage.normalUrl,
			normalKey = baseImage.normalKey,
			miniUrl = baseImage.miniUrl ?: "",
			miniKey = baseImage.miniKey ?: "",
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
		val images = medalDetailsEntity.images

		var medalImageEntity = images.firstOrNull() ?: run {
			medalEntity.mainImg = null
			return null
		}

		images.forEach {
			if (it.createdAt > medalImageEntity.createdAt) {
				medalImageEntity = it
			} else if (it.main) {
				it.main = false
			}
		}
		medalImageEntity.main = true
		medalEntity.mainImg = medalImageEntity.miniUrl
		return medalImageEntity.toBaseImage()
	}

	@Transactional
	override fun deleteImage(medalId: Long, imageId: Long): BaseImage {
		val awardImageEntity = medalImageRepository.findByIdAndMedalId(medalId = medalId, imageId = imageId) ?: run {
			throw ImageNotFoundException()
		}
		medalImageRepository.delete(awardImageEntity)
		return awardImageEntity.toBaseImage()
	}

	override fun addGalleryImage(medalId: Long, smallItem: SmallItem): BaseImage {
		val medalImageEntity = MedalImageEntity(
			medalId = medalId,
			normalUrl = smallItem.imageUrl,
			miniUrl = smallItem.miniUrl,
			type = ImageType.SYSTEM,
			createdAt = LocalDateTime.now()
		)
		medalImageRepository.save(medalImageEntity)
		return medalImageEntity.toBaseImage()
	}

}