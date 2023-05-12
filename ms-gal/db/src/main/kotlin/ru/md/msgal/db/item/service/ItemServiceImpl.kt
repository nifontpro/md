package ru.md.msgal.db.item.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.md.msgal.db.item.repo.ItemRepository
import ru.md.msgal.domain.item.service.ItemService

@Service
@Transactional
class ItemServiceImpl(
	private val itemRepository: ItemRepository
) : ItemService {

}