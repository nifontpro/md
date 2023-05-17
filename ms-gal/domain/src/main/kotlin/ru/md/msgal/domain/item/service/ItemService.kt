package ru.md.msgal.domain.item.service

import ru.md.msgal.domain.item.model.Item

interface ItemService {
	fun create(item: Item): Item
}