package ru.md.msc.domain.user.service

import ru.md.msc.domain.image.model.BaseImage
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails

interface UserService {
	fun createOwner(userDetails: UserDetails): UserDetails
	fun create(userDetails: UserDetails): UserDetails
	fun deleteById(userId: Long)
	fun update(userDetails: UserDetails, isAuthUserHasAdminRole: Boolean): UserDetails
	fun doesOwnerWithEmailExist(email: String): Boolean
	fun findById(userId: Long): User?
	fun findByAuthEmailWithDept(authEmail: String): List<User>
	fun findByDeptId(deptId: Long): List<User>
	fun findByIdDetails(userId: Long): UserDetails?
	fun addImage(userId: Long, baseImage: BaseImage): BaseImage
	fun deleteImage(userId: Long, imageId: Long): BaseImage
	fun findDeptIdByUserId(userId: Long): Long
}