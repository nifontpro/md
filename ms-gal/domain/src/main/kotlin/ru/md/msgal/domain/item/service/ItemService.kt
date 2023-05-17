package ru.md.msgal.domain.item.service

import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.msgal.domain.item.model.Item

interface ItemService {
	fun create(item: Item): Item
	fun getByFolderId(folderId: Long, baseQuery: BaseQuery): PageResult<Item>
	fun getById(itemId: Long): Item
}