package ru.md.msgal.rest.item.model.request

import ru.md.base_rest.model.request.BaseRequest

data class GetItemsByFolderRequest (
	val folderId: Long = 0,
	val baseRequest: BaseRequest = BaseRequest()
)