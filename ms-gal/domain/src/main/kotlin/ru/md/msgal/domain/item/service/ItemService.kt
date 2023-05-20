package ru.md.msgal.domain.item.service

import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.base_domain.gallery.GalleryItem

interface ItemService {
	fun create(galleryItem: GalleryItem): GalleryItem
	fun getByFolderId(folderId: Long, baseQuery: BaseQuery): PageResult<GalleryItem>
	fun getById(itemId: Long): GalleryItem
}