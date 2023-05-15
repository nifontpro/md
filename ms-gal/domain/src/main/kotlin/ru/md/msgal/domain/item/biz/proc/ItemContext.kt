package ru.md.msgal.domain.item.biz.proc

import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.msgal.domain.base.biz.BaseGalleryContext
import ru.md.msgal.domain.item.model.Item
import ru.md.msgal.domain.item.service.ItemService

class ItemContext : BaseGalleryContext() {
	var item: Item = Item()
	var items: List<Item> = emptyList()
	var itemId: Long = 0
	lateinit var itemService: ItemService
}

enum class ItemCommand : IBaseCommand {
	CREATE,
	DELETE,
	UPDATE,
	GET_BY_ID_DETAILS,
}
