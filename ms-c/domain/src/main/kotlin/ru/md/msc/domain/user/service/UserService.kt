package ru.md.msc.domain.user.service

import ru.md.msc.domain.image.model.BaseImage
import ru.md.msc.domain.image.model.FileData
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails

interface UserService {
	fun createOwner(userDetails: UserDetails): UserDetails
	fun doesOwnerWithEmailExist(email: String): Boolean
	fun findById(userId: Long): User?
	fun findByAuthEmailWithDept(authEmail: String): List<User>
	fun deleteById(userId: Long)
	fun findByDeptId(deptId: Long): List<User>
	fun findByIdDetails(userId: Long): UserDetails?
	suspend fun addImage(userId: Long, fileData: FileData): BaseImage
	suspend fun deleteImage(userId: Long, imageId: Long): BaseImage
	fun create(userDetails: UserDetails): UserDetails
	suspend fun updateImage(userId: Long, imageId: Long, fileData: FileData): BaseImage
	fun update(userDetails: UserDetails, isAuthUserHasAdminRole: Boolean): UserDetails
}