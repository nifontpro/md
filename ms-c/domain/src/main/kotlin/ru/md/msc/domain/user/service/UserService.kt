package ru.md.msc.domain.user.service

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.msc.domain.user.model.GenderCount
import ru.md.msc.domain.user.model.User
import ru.md.msc.domain.user.model.UserDetails
import ru.md.msc.domain.user.model.UserSettings

interface UserService {
	fun createOwner(userDetails: UserDetails): UserDetails
	fun create(userDetails: UserDetails): UserDetails
	fun deleteById(userId: Long)
	fun update(userDetails: UserDetails, isAuthUserHasAdminRole: Boolean): UserDetails
	fun doesOwnerWithEmailExist(email: String): Boolean
	fun findById(userId: Long): User?
	fun findByAuthEmailWithDept(authEmail: String): List<User>
	fun findByIdDetails(userId: Long): UserDetails?
	fun addImage(userId: Long, baseImage: BaseImage): BaseImage
	fun deleteImage(userId: Long, imageId: Long): BaseImage
	fun findDeptIdByUserId(userId: Long): Long
	fun findBySubDepts(deptId: Long, baseQuery: BaseQuery): PageResult<User>
	fun findByDeptId(deptId: Long, baseQuery: BaseQuery): PageResult<User>
	fun getGenderCountByDept(deptId: Long, subdepts: Boolean): GenderCount
	fun getUsersWithActivity(deptId: Long, baseQuery: BaseQuery): List<User>
	fun getUsersWithAward(deptId: Long, baseQuery: BaseQuery): List<User>
	fun setMainImage(userId: Long): BaseImage?
	fun updateAllUserImg()
	fun getUsersWithAwardCount(deptId: Long, baseQuery: BaseQuery): PageResult<User>
	fun saveSettings(userSettings: UserSettings): UserSettings
	fun getSettings(userId: Long): UserSettings?
}