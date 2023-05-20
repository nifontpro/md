package ru.md.msgal.rest.item.mappers

import ru.md.base_rest.model.mapper.toBaseQuery
import ru.md.msgal.domain.item.biz.proc.ItemCommand
import ru.md.msgal.domain.item.biz.proc.ItemContext
import ru.md.base_domain.gallery.request.GetItemByIdRequest
import ru.md.msgal.rest.item.model.request.GetItemsByFolderRequest

fun ItemContext.fromTransport(request: GetItemsByFolderRequest) {
	command = ItemCommand.GET_BY_FOLDER
	folderId = request.folderId
	baseQuery = request.baseRequest.toBaseQuery()
}

fun ItemContext.fromTransport(request: GetItemByIdRequest) {
	command = ItemCommand.GET_BY_ID
	itemId = request.itemId
}

