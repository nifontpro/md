package ru.md.msc.db.user.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.mapper.toImage
import ru.md.base_db.mapper.toPageRequest
import ru.md.base_db.mapper.toPageResult
import ru.md.base_db.mapper.toSearch
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.msc.db.dept.model.DeptDetailsEntity
import ru.md.msc.db.dept.model.DeptEntity
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.db.user.model.UserDetailsEntity
import ru.md.msc.db.user.model.image.UserImageEntity
import ru.md.msc.db.user.model.mappers.*
import ru.md.msc.db.user.model.role.RoleEntity
import ru.md.msc.db.user.repo.*
import ru.md.msc.domain.base.biz.ImageNotFoundException
import ru.md.msc.domain.dept.model.DeptType
import ru.md.msc.domain.user.biz.proc.UserNotFoundException
import ru.md.msc.domain.user.model.*
import ru.md.msc.domain.user.service.UserService
import java.time.LocalDateTime

// id корневого отдела программы
const val ROOT_DEPT_ID = 1L

@Service
@Transactional
class UserServiceImpl(
	private val userRepository: UserRepository,
	private val userDetailsRepository: UserDetailsRepository,
	private val roleRepository: RoleRepository,
	private val deptRepository: DeptRepository,
	private val deptDetailsRepository: DeptDetailsRepository,
	private val userImageRepository: UserImageRepository,
	private val userSettingsRepository: UserSettingsRepository
) : UserService {

	/**
	 * Создание корневого владельца вместе с отделом
	 */
	override fun createOwner(userDetails: UserDetails): UserDetails {

		val userDetailsEntity = userDetails.toUserDetailsEntity(create = true)

		val deptEntity = DeptEntity(
			parentId = ROOT_DEPT_ID,
			name = "Владелец " + userDetails.user.authEmail,
			classname = "Корневой",
			type = DeptType.USER_OWNER,
		)
		val deptDetailsEntity = DeptDetailsEntity(
			dept = deptEntity,
			address = userDetails.address,
			email = userDetails.user.authEmail,
			phone = userDetails.phone,
			createdAt = LocalDateTime.now()
		)

		deptDetailsRepository.save(deptDetailsEntity)
		userDetailsEntity.user?.dept = deptEntity
		userDetailsRepository.save(userDetailsEntity)
		addRolesToUserEntity(userDetails, userDetailsEntity)

		return userDetailsEntity.toUserDetails()
	}

	override fun create(userDetails: UserDetails): UserDetails {
		val userDetailsEntity = userDetails.toUserDetailsEntity(create = true)
		userDetailsRepository.save(userDetailsEntity)
		addRolesToUserEntity(userDetails, userDetailsEntity)
		return userDetailsEntity.toUserDetails()
	}

	override fun update(userDetails: UserDetails, isAuthUserHasAdminRole: Boolean): UserDetails {
		val oldUserDetailsEntity = userDetailsRepository.findByUserId(userDetails.user.id) ?: throw UserNotFoundException()
		with(oldUserDetailsEntity) {
			user?.let {
				it.firstname = userDetails.user.firstname
				it.patronymic = userDetails.user.patronymic
				it.lastname = userDetails.user.lastname
				if (isAuthUserHasAdminRole) {
					it.authEmail = userDetails.user.authEmail
				}
				it.gender = userDetails.user.gender
				it.post = userDetails.user.post
			}
			phone = userDetails.phone
			address = userDetails.address
			description = userDetails.description
		}
//		if (isAuthUserHasAdminRole) {
//			addRolesToUserEntity(userDetails, oldUserDetailsEntity)
//		}
		return oldUserDetailsEntity.toUserDetails()
	}

	private fun addRolesToUserEntity(
		userDetails: UserDetails,
		userDetailsEntity: UserDetailsEntity
	) {
		val roles = userDetails.user.roles.map { roleEnum ->
			RoleEntity(roleUser = roleEnum, user = userDetailsEntity.user)
		}
		userDetailsEntity.user?.roles?.let {
			it.removeAll { true }
			it.addAll(roles)
		}
	}

	override fun doesOwnerWithEmailExist(email: String): Boolean {
		val roles = roleRepository.findByRoleUserAndUserAuthEmail(
			roleUser = RoleUser.OWNER,
			userEmail = email
		)
		return roles.isNotEmpty()
	}

	override fun findByAuthEmailWithDept(authEmail: String): List<User> {
		return userRepository.findByAuthEmailIgnoreCase(authEmail = authEmail).map {
			it.toUser()
		}
	}

	override fun findByDeptId(deptId: Long, baseQuery: BaseQuery): PageResult<User> {
		val pageRequest = baseQuery.toPageRequest()
		val res = userRepository.findByDeptIdAndLastnameLikeIgnoreCase(
			deptId = deptId,
			lastname = baseQuery.filter.toSearch(),
			pageable = pageRequest
		)
		return res.toPageResult { it.toUserLazy() }
	}

	override fun findBySubDepts(deptId: Long, baseQuery: BaseQuery): PageResult<User> {
		val pageRequest = baseQuery.toPageRequest()
		val deptsIds = deptRepository.subTreeIds(deptId = deptId)
		val res = userRepository.findByDeptIdInAndLastnameLikeIgnoreCase(
			deptsIds = deptsIds,
			lastname = baseQuery.filter.toSearch(),
			pageable = pageRequest
		)
		return res.toPageResult { it.toUserWithDeptOnly() }
	}

	override fun findById(userId: Long): User? {
		return userRepository.findByIdOrNull(userId)?.toUserOnlyRoles()
	}

	override fun findByIdDetails(userId: Long): UserDetails? {
		return userDetailsRepository.findByUserId(userId)?.toUserDetails()
	}

	override fun deleteById(userId: Long) {
		userRepository.deleteById(userId)
	}

	override fun addImage(userId: Long, baseImage: BaseImage): BaseImage {
		val userImageEntity = UserImageEntity(
			userId = userId,
			imageUrl = baseImage.imageUrl,
			imageKey = baseImage.imageKey,
			type = baseImage.type,
			createdAt = LocalDateTime.now()
		)
		userImageRepository.save(userImageEntity)
		return userImageEntity.toImage()
	}

	override fun deleteImage(userId: Long, imageId: Long): BaseImage {
		val userImageEntity = userImageRepository.findByIdAndUserId(userId = userId, imageId = imageId) ?: run {
			throw ImageNotFoundException()
		}
		userImageRepository.delete(userImageEntity)
		return userImageEntity.toImage()
	}

	override fun setMainImage(userId: Long): BaseImage? {
		val userEntity = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
		val images = userEntity.images
		var userImageEntity = images.firstOrNull() ?: run {
			userEntity.mainImg = null
			return null
		}

		images.forEach {
			if (it.createdAt > userImageEntity.createdAt) {
				userImageEntity = it
			}
		}
		userEntity.mainImg = userImageEntity.imageUrl
		return userImageEntity.toImage()
	}

	override fun updateAllUserImg() {
		val users = userRepository.findAll()
		users.forEach {
			val id = it.id ?: return@forEach
			val img = setMainImage(id)
			println(img)
		}
	}

	override fun findDeptIdByUserId(userId: Long): Long {
		return userRepository.finDeptId(userId = userId) ?: throw UserNotFoundException()
	}

	override fun getGenderCountByDept(deptId: Long, subdepts: Boolean): GenderCount {
		val deptsIds = if (subdepts) {
			deptRepository.subTreeIds(deptId = deptId)
		} else {
			listOf(deptId)
		}
		return userRepository.genderCount(deptsIds = deptsIds)
	}

	override fun getUsersWithActivity(deptId: Long, baseQuery: BaseQuery): List<User> {
		val deptsIds = if (baseQuery.subdepts) {
			deptRepository.subTreeIds(deptId = deptId)
		} else {
			listOf(deptId)
		}
		return userRepository.findByDeptIdIn(deptsIds = deptsIds).map { it.toUserActivity() }
	}

	override fun getUsersWithAward(deptId: Long, baseQuery: BaseQuery): List<User> {
		val deptsIds = if (baseQuery.subdepts) {
			deptRepository.subTreeIds(deptId = deptId)
		} else {
			listOf(deptId)
		}
		return userRepository.findByDeptIdIn(deptsIds = deptsIds).map { it.toUserAward() }
	}

	override fun getUsersWithAwardCount(deptId: Long, baseQuery: BaseQuery): PageResult<User> {
		val deptsIds = if (baseQuery.subdepts) {
			deptRepository.subTreeIds(deptId = deptId)
		} else {
			listOf(deptId)
		}
		val users = userRepository.findUsersWithAwardCount(
			deptsIds = deptsIds,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
			pageable = baseQuery.toPageRequest()
		)
		return users.toPageResult { it.toUser() }
	}

	override fun saveSettings(userSettings: UserSettings): UserSettings {
		val userSettingsEntity = userSettings.toUserSettingsEntity()
		userSettingsRepository.save(userSettingsEntity)
		return userSettingsEntity.toUserSettings()
	}

	override fun getSettings(userId: Long): UserSettings? {
		return userSettingsRepository.findByIdOrNull(userId)?.toUserSettings()
	}

//	companion object {
//		val log: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)
//	}

}