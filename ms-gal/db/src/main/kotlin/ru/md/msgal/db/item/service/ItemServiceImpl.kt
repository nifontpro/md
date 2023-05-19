package ru.md.msgal.db.item.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.mapper.toPageRequest
import ru.md.base_db.mapper.toPageResult
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.msgal.db.item.model.mapper.toItem
import ru.md.msgal.db.item.model.mapper.toItemEntity
import ru.md.msgal.db.item.repo.ItemRepository
import ru.md.msgal.domain.item.biz.proc.ItemNotFoundException
import ru.md.base_domain.item.GalleryItem
import ru.md.msgal.domain.item.service.ItemService

@Service
@Transactional
class ItemServiceImpl(
	private val itemRepository: ItemRepository
) : ItemService {

	override fun create(galleryItem: GalleryItem): GalleryItem {
		val itemEntity = galleryItem.toItemEntity(create = true)
		itemRepository.save(itemEntity)
		return itemEntity.toItem()
	}

	override fun getByFolderId(folderId: Long, baseQuery: BaseQuery): PageResult<GalleryItem> {
		val items = itemRepository.findByFolderId(folderId = folderId, pageable = baseQuery.toPageRequest())
		return items.toPageResult { it.toItem() }
	}

	override fun getById(itemId: Long): GalleryItem {
		val itemEntity = itemRepository.findByIdOrNull(id = itemId) ?: throw ItemNotFoundException()
		return itemEntity.toItem()
	}

}