package ru.md.msc.rest.user.mappers

import ru.md.msc.domain.user.biz.proc.UserContext
import ru.md.msc.domain.user.model.User

fun UserContext.toTransportGetUser(): User = user

