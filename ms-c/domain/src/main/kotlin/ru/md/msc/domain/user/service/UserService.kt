package ru.md.msc.domain.user.service

import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.base_domain.user.model.User
import ru.md.msc.domain.award.model.ActionType
import ru.md.msc.domain.user.model.*

interface UserService {
	fun createOwner(userDetails: UserDetails): UserDetailsDept
	fun create(userDetails: UserDetails): UserDetails
	fun deleteById(userId: Long, deptId: Long?)
	fun update(userDetails: UserDetails, isAuthUserHasAdminRole: Boolean): UserDetails
	fun doesOwnerWithEmailExist(email: String): Boolean
	fun findByAuthEmailWithDept(authEmail: String): List<User>
	fun findByIdDetails(userId: Long): UserDetails?
	fun addImage(userId: Long, baseImage: BaseImage): BaseImage
	fun deleteImage(userId: Long, imageId: Long): BaseImage
	fun findBySubDepts(deptId: Long, baseQuery: BaseQuery): PageResult<User>
	fun getGenderCountByDept(deptId: Long, subdepts: Boolean): GenderCount
	fun getUsersWithActivity(deptId: Long, baseQuery: BaseQuery): List<UserAward>
	fun getUsersWithAward(deptId: Long, baseQuery: BaseQuery): List<UserAward>
	fun setMainImage(userId: Long): BaseImage?
	fun updateAllUserImg()
	fun getUsersWithAwardCount(deptId: Long, baseQuery: BaseQuery): PageResult<User>
	fun saveSettings(userSettings: UserSettings): UserSettings
	fun getSettings(userId: Long): UserSettings?
	fun findByDeptsExclude(deptId: Long, awardId: Long, actionType: ActionType?, baseQuery: BaseQuery): PageResult<User>
	fun doesUserOwnerRole(userId: Long): Boolean
	fun activityByUserExist(userId: Long): Boolean
	fun moveUserToArchive(userId: Long)
	fun validateEmail(deptId: Long, email: String): Boolean
	fun validateByDeptIdAndEmailExist(deptId: Long, email: String): Boolean
	fun simpleUpdate(userDetails: UserDetails): UserDetails
}