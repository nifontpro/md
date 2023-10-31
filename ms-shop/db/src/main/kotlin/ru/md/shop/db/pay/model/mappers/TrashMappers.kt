package ru.md.shop.db.pay.model.mappers

import ru.md.shop.db.pay.model.TrashEntity
import ru.md.shop.domain.pay.model.Trash

fun TrashEntity.toTrash() = Trash(
	id = id,
	userId = userId,
	productId = productId,
	addAt = addAt,
	quantity = quantity
)