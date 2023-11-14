package ru.md.base_db.dirty.model.mappers

import ru.md.base_db.dirty.model.DirtyLinkEntity
import ru.md.base_domain.dirty.model.DirtyLink

fun DirtyLink.toDirtyLinkEntity() = DirtyLinkEntity(
	id = if (id == 0L) null else id,
	key = key,
	repo = repo,
	saveAs = saveAs
)

fun DirtyLinkEntity.toDirtyLink() = DirtyLink(
	id = id ?: 0,
	key = key,
	repo = repo,
	saveAs = saveAs
)