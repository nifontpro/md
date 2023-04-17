package ru.md.msc.domain.user.service

import ru.md.msc.domain.base.model.RepositoryData
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails

interface UserService {
	fun getAll(): List<User>
	fun createOwner(userDetails: UserDetails): RepositoryData<UserDetails>
	fun doesOwnerWithEmailExist(email: String): RepositoryData<Boolean>
	fun findById(userId: Long): RepositoryData<User>
	fun create(userDetails: UserDetails): RepositoryData<UserDetails>
	fun findByAuthEmailWithDept(authEmail: String): RepositoryData<List<User>>
	fun deleteById(userId: Long): RepositoryData<UserDetails>
	fun findByDeptId(deptId: Long): RepositoryData<List<User>>
}