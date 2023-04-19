package ru.md.msc.domain.user.service

import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails

interface UserService {
	fun createOwner(userDetails: UserDetails): UserDetails
	fun doesOwnerWithEmailExist(email: String): Boolean
	fun findById(userId: Long): User?
	fun create(userDetails: UserDetails): UserDetails
	fun findByAuthEmailWithDept(authEmail: String): List<User>
	fun deleteById(userId: Long)
	fun findByDeptId(deptId: Long): List<User>
	fun findByIdDetails(userId: Long): UserDetails?
}