package ru.md.msgal.rest.folder.mappers

import ru.md.base_rest.model.BaseResponse
import ru.md.base_rest.model.baseResponse
import ru.md.msgal.domain.folder.biz.proc.FolderContext
import ru.md.msgal.domain.folder.model.Folder

fun FolderContext.toTransportFolder(): BaseResponse<Folder> {
	return baseResponse(folder)
}
