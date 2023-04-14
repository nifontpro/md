package ru.md.msc.domain.user.service

import ru.md.msc.domain.base.model.RepositoryData
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails

interface UserService {
	fun getAll(): List<User>
	fun createOwner(userDetails: UserDetails): RepositoryData<UserDetails>
	fun doesOwnerWithEmailExist(email: String): RepositoryData<Boolean>
	fun getById(userId: Long): RepositoryData<User>
	fun create(userDetails: UserDetails): RepositoryData<UserDetails>
	fun findByEmailWithDept(email: String): RepositoryData<List<User>>
}