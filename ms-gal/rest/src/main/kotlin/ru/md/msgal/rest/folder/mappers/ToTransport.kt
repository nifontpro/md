package ru.md.msgal.rest.folder.mappers

import ru.md.base_domain.rest.BaseResponse
import ru.md.base_domain.rest.baseResponse
import ru.md.msgal.domain.folder.biz.proc.FolderContext
import ru.md.msgal.rest.folder.model.response.FolderResponse

fun FolderContext.toTransportFolder(): BaseResponse<FolderResponse> {
	return baseResponse(folder.toFolderResponse())
}

fun FolderContext.toTransportFolders(): BaseResponse<List<FolderResponse>> {
	return baseResponse(folders.map { it.toFolderResponse() })
}

