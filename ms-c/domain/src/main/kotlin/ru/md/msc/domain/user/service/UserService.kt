package ru.md.msc.domain.user.service

import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails

interface UserService {
	fun getAll(): List<User>
	fun add(userDetails: UserDetails)
}