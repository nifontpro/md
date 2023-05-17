package ru.md.msgal.db.item.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.md.msgal.db.item.model.mapper.toItem
import ru.md.msgal.db.item.model.mapper.toItemEntity
import ru.md.msgal.db.item.repo.ItemRepository
import ru.md.msgal.domain.item.model.Item
import ru.md.msgal.domain.item.service.ItemService

@Service
@Transactional
class ItemServiceImpl(
	private val itemRepository: ItemRepository
) : ItemService {

	override fun create(item: Item): Item {
		val itemEntity = item.toItemEntity(create = true)
		itemRepository.save(itemEntity)
		return itemEntity.toItem()
	}

}