package ru.md.msc.rest.user.mappers

import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.rest.base.BaseResponse
import ru.md.msc.rest.base.baseResponse

fun UserContext.toTransportGetUserDetails(): BaseResponse<UserDetails> = baseResponse(userDetails)
