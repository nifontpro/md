package ru.md.msgal.domain.item.biz.proc

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.md.base_domain.biz.proc.IBaseCommand
import ru.md.base_domain.gallery.GalleryItem
import ru.md.msgal.domain.base.biz.BaseGalleryContext
import ru.md.msgal.domain.item.service.ItemService

class ItemContext : BaseGalleryContext() {
	var item: GalleryItem = GalleryItem()
	var items: List<GalleryItem> = emptyList()
	var itemId: Long = 0

	override val log: Logger = LoggerFactory.getLogger(ItemContext::class.java)
	lateinit var itemService: ItemService
}

enum class ItemCommand : IBaseCommand {
	CREATE,
	GET_BY_FOLDER,
	GET_BY_ID,
}
