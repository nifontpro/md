package ru.md.msgal.domain.item.biz.proc

import ru.md.msgal.domain.base.biz.BaseContext
import ru.md.msgal.domain.base.biz.IBaseCommand
import ru.md.msgal.domain.item.model.Item
import ru.md.msgal.domain.item.service.ItemService

class ItemContext : BaseContext() {
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
