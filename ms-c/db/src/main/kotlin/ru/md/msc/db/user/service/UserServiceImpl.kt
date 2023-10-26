package ru.md.msc.db.user.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.mapper.*
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.msc.db.award.repo.ActivityRepository
import ru.md.msc.db.dept.model.DeptDetailsEntity
import ru.md.base_db.dept.model.DeptEntity
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.db.dept.service.DeptUtil
import ru.md.msc.db.user.model.UserDetailsEntity
import ru.md.base_db.user.model.UserImageEntity
import ru.md.msc.db.user.model.mappers.*
import ru.md.base_db.user.model.RoleEntity
import ru.md.base_db.user.model.mappers.toUser
import ru.md.base_db.user.model.mappers.toUserWithDeptOnly
import ru.md.msc.db.user.repo.*
import ru.md.msc.domain.award.model.ActionType
import ru.md.base_domain.errors.ImageNotFoundException
import ru.md.base_domain.dept.model.DeptType
import ru.md.base_domain.user.model.RoleUser
import ru.md.base_domain.user.model.User
import ru.md.base_domain.user.biz.errors.UserNotFoundException
import ru.md.msc.domain.user.model.*
import ru.md.msc.domain.user.service.UserService
import java.time.LocalDateTime

// id корневого отдела программы
const val ROOT_DEPT_ID = 1L

@Service
@Transactional
class UserServiceImpl(
	private val userRepository: UserRepository,
	private val userAwardRepository: UserAwardRepository,
	private val userDetailsRepository: UserDetailsRepository,
	private val roleRepository: RoleRepository,
	private val deptRepository: DeptRepository,
	private val deptDetailsRepository: DeptDetailsRepository,
	private val userImageRepository: UserImageRepository,
	private val userSettingsRepository: UserSettingsRepository,
	private val activityRepository: ActivityRepository,
	private val deptUtil: DeptUtil,
) : UserService {

	/**
	 * Создание корневого владельца вместе с отделом
	 */
	override fun createOwner(userDetails: UserDetails): UserDetailsDept {

		val userDetailsEntity = userDetails.toUserDetailsEntity(create = true)

		val deptEntity = DeptEntity(
			parentId = ROOT_DEPT_ID,
			name = "Корневой отдел",
			classname = "Корневой",
			type = DeptType.USER_OWNER,
			topLevel = true
		)
		val deptDetailsEntity = DeptDetailsEntity(
			dept = deptEntity,
			address = userDetails.address,
			email = userDetails.user.authEmail,
			phone = userDetails.phone,
			createdAt = LocalDateTime.now()
		)

		deptDetailsRepository.saveAndFlush(deptDetailsEntity)
		userDetailsEntity.user?.dept = deptEntity
		userDetailsRepository.save(userDetailsEntity)
		addRolesToUserEntity(userDetails, userDetailsEntity)

		// Создание первого отдела (компании) по умолчанию
		val newDeptEntity = DeptEntity(
			parentId = deptEntity.id,
			name = "Новая компания",
			classname = "Компания",
			type = DeptType.SIMPLE,
			topLevel = true
		)
		val newDeptDetailsEntity = DeptDetailsEntity(
			dept = newDeptEntity,
			createdAt = LocalDateTime.now()
		)
		deptDetailsRepository.saveAndFlush(newDeptDetailsEntity)
		return UserDetailsDept(
			userDetails = userDetailsEntity.toUserDetails(),
			deptId = newDeptDetailsEntity.dept?.id ?: 0
		)
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
				val targetDeptId = userDetails.user.dept?.id
				if (targetDeptId != 0L && targetDeptId != oldUserDetailsEntity.user?.dept?.id) {
					it.dept = DeptEntity(id = userDetails.user.dept?.id)
				}
			}
			phone = userDetails.phone
			address = userDetails.address
			description = userDetails.description
		}
		if (userDetails.user.roles.isNotEmpty() && isAuthUserHasAdminRole) {
			addRolesToUserEntity(userDetails, oldUserDetailsEntity)
		}
		return oldUserDetailsEntity.toUserDetails()
	}

	/**
	 * Обновление основных полей с сохранением ролей, изображений и т. д.
	 */
	override fun simpleUpdate(userDetails: UserDetails): UserDetails {
		val oldUserDetailsEntity = userDetailsRepository
			.findByUserAuthEmail(userDetails.user.authEmail ?: "") ?: throw UserNotFoundException()
		with(oldUserDetailsEntity) {
			user?.let {
				it.firstname = userDetails.user.firstname
				it.patronymic = userDetails.user.patronymic
				it.lastname = userDetails.user.lastname
				it.post = userDetails.user.post
			}
			phone = userDetails.phone
//			address = userDetails.address
			description = userDetails.description
		}
		return oldUserDetailsEntity.toUserDetails()
	}

	private fun addRolesToUserEntity(
		userDetails: UserDetails,
		userDetailsEntity: UserDetailsEntity
	) {
		val roles = userDetails.user.roles.map { roleEnum ->
			RoleEntity(roleUser = roleEnum, user = userDetailsEntity.user)
		}
//		userDetailsEntity.user?.roles = roles.toMutableList()
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

	override fun findBySubDepts(deptId: Long, baseQuery: BaseQuery): PageResult<User> {
		val pageRequest = baseQuery.toPageRequest()
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)
		val res = userRepository.findByDeptIdInAndLastnameLikeIgnoreCase(
			deptsIds = deptsIds,
			lastname = baseQuery.filter.toSearch(),
			pageable = pageRequest
		)
		return res.toPageResult { it.toUserWithDeptOnly() }
	}

	override fun findByDeptsExclude(
		deptId: Long,
		awardId: Long,
		actionType: ActionType?,
		baseQuery: BaseQuery
	): PageResult<User> {
		val pageRequest = baseQuery.toPageRequest()
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)

		val filter = baseQuery.filter.toSearchOrNull()

		val excludeUsersIds = activityRepository.findActivityUserIdsByAwardId(
			awardId = awardId,
			filter = filter,
			actionType = actionType
		)

		val res = userRepository.findByDeptIdExcludeIds(
			deptsIds = deptsIds,
			filter = filter,
//			usersIds = excludeUsersIds.ifEmpty { null },
			notExclude = excludeUsersIds.isEmpty(),
			usersIds = excludeUsersIds,
			pageable = pageRequest
		)
		return res.toPageResult { it.toUserWithDeptOnly() }
	}

//	override fun findById(userId: Long): User? {
//		return userRepository.findByIdOrNull(userId)?.toUserOnlyRoles()
//	}

	override fun findByIdDetails(userId: Long): UserDetails? {
		return userDetailsRepository.findByUserId(userId)?.toUserDetails()
	}

	override fun deleteById(userId: Long, deptId: Long?) {
		userRepository.deleteById(userId)
		deptId?.let { deptRepository.deleteById(it) } // Если Владелец, удаляем его отдел
	}

	override fun addImage(userId: Long, baseImage: BaseImage): BaseImage {
		val userImageEntity = UserImageEntity(
			userId = userId,
			originUrl = baseImage.originUrl,
			originKey = baseImage.originKey,
			normalUrl = baseImage.normalUrl,
			normalKey = baseImage.normalKey,
			miniUrl = baseImage.miniUrl,
			miniKey = baseImage.miniKey,
			type = baseImage.type,
			createdAt = LocalDateTime.now()
		)
		userImageRepository.save(userImageEntity)
		return userImageEntity.toBaseImage()
	}

	override fun deleteImage(userId: Long, imageId: Long): BaseImage {
		val userImageEntity = userImageRepository.findByIdAndUserId(userId = userId, imageId = imageId) ?: run {
			throw ImageNotFoundException()
		}
		userImageRepository.delete(userImageEntity)
		return userImageEntity.toBaseImage()
	}

	override fun setMainImage(userId: Long): BaseImage? {
		val userEntity = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
		val images = userEntity.images
		var userImageEntity = images.firstOrNull() ?: run {
			// +
			userEntity.mainImg = null
			return null
		}

		images.forEach {
			if (it.createdAt > userImageEntity.createdAt) {
				userImageEntity = it
			} else if (it.main) {
				it.main = false
			}
		}
		userEntity.mainImg = userImageEntity.miniUrl
		// +
		userImageEntity.main = true
		return userImageEntity.toBaseImage()
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
		return userRepository.findDeptId(userId = userId) ?: throw UserNotFoundException()
	}

	override fun getGenderCountByDept(deptId: Long, subdepts: Boolean): GenderCount {
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = subdepts)
		return userRepository.genderCount(deptsIds = deptsIds)
	}

	override fun getUsersWithActivity(deptId: Long, baseQuery: BaseQuery): List<UserAward> {
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)
		return userAwardRepository.findByDeptIdIn(deptsIds = deptsIds).map { it.toUserActivity() }
	}

	override fun getUsersWithAward(deptId: Long, baseQuery: BaseQuery): List<UserAward> {
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)
		return userAwardRepository.findByDeptIdIn(deptsIds = deptsIds).map { it.toUserAward() }
	}

	override fun getUsersWithAwardCount(deptId: Long, baseQuery: BaseQuery): PageResult<User> {
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)
		val users = userRepository.findUsersWithAwardCount(
			deptsIds = deptsIds,
			filter = baseQuery.filter.toSearchOrNull(),
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

	/**
	 * Проверка, имеет ли сотрудник роль Владельца
	 */
	override fun doesUserOwnerRole(userId: Long): Boolean {
		return roleRepository.countByUserIdAndRoleUser(
			userId = userId,
			roleUser = RoleUser.OWNER
		) > 0
	}

	override fun activityByUserExist(userId: Long): Boolean {
		return activityRepository.countByUserIdAndActiv(userId = userId) > 0
	}

	override fun moveUserToArchive(userId: Long) {
		userRepository.moveUserToArchive(userId = userId)
	}

	/**
	 * Проверка есть ли заданный email во всем дереве отделов
	 */
	override fun validateEmail(deptId: Long, email: String): Boolean {
		val deptIds = deptUtil.getAllDeptIds(deptId)
		return userRepository.countByAuthEmailIgnoreCaseAndDeptIdIn(
			authEmail = email, deptsIds = deptIds
		) > 0
	}

	/**
	 * Проверка, существует ли сотрудник с email в отделе
	 */
	override fun validateByDeptIdAndEmailExist(deptId: Long, email: String): Boolean {
		return userRepository.countByDeptIdAndAuthEmailIgnoreCase(
			authEmail = email, deptId = deptId
		) > 0
	}

//	companion object {
//		val log: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)
//	}

}