package ru.md.msc.db.user.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.base.mapper.toPageRequest
import ru.md.base_db.base.mapper.toPageResult
import ru.md.base_db.base.mapper.toSearch
import ru.md.base_db.base.mapper.toSearchUpperOrNull
import ru.md.base_db.dept.model.DeptEntity
import ru.md.base_db.dept.model.mappers.toDeptEntity
import ru.md.base_db.image.mappers.toBaseImage
import ru.md.base_db.user.model.RoleEntity
import ru.md.base_db.user.model.UserImageEntity
import ru.md.base_db.user.model.converter.toGender
import ru.md.base_db.user.model.mappers.toUserWithDeptAndCompany
import ru.md.base_db.user.model.mappers.toUserWithDeptOnly
import ru.md.base_domain.dept.model.DeptType
import ru.md.base_domain.errors.ImageNotFoundException
import ru.md.base_domain.image.model.BaseImage
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.PageResult
import ru.md.base_domain.user.biz.errors.UserNotFoundException
import ru.md.base_domain.user.model.Gender
import ru.md.base_domain.user.model.RoleUser
import ru.md.base_domain.user.model.User
import ru.md.msc.db.award.repo.ActivityRepository
import ru.md.msc.db.dept.model.DeptDetailsEntity
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.db.dept.service.DeptUtil
import ru.md.msc.db.user.model.UserDetailsEntity
import ru.md.msc.db.user.model.mappers.*
import ru.md.msc.db.user.repo.*
import ru.md.msc.domain.award.model.ActionType
import ru.md.msc.domain.user.model.*
import ru.md.msc.domain.user.service.UserService
import java.time.LocalDateTime

// id корневого отдела программы
const val ROOT_DEPT_ID = 1L

@Service
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
	@Transactional
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
		userDetailsEntity.user.dept = deptEntity
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
			deptId = newDeptDetailsEntity.dept.id ?: 0
		)
	}

	@Transactional
	override fun create(userDetails: UserDetails): UserDetails {
		val userDetailsEntity = userDetails.toUserDetailsEntity(create = true)
		userDetailsRepository.save(userDetailsEntity)
		addRolesToUserEntity(userDetails, userDetailsEntity)
		return userDetailsEntity.toUserDetails()
	}

	@Transactional
	override fun update(userDetails: UserDetails, isAuthUserHasAdminRole: Boolean): UserDetails {
		val oldUserDetailsEntity = userDetailsRepository.findByUserId(userDetails.user.id) ?: throw UserNotFoundException()
		with(oldUserDetailsEntity) {
			user.firstname = userDetails.user.firstname
			user.patronymic = userDetails.user.patronymic
			user.lastname = userDetails.user.lastname
			if (isAuthUserHasAdminRole) {
				user.authEmail = userDetails.user.authEmail
			}
			user.gender = userDetails.user.gender
			user.post = userDetails.user.post
			val targetDeptId = userDetails.user.dept?.id
			if (targetDeptId != 0L && targetDeptId != oldUserDetailsEntity.user.dept?.id) {
				user.dept = DeptEntity(id = userDetails.user.dept?.id)
			}
			phone = userDetails.phone
			address = userDetails.address
			description = userDetails.description
			schedule = userDetails.schedule
		}
		if (userDetails.user.roles.isNotEmpty() && isAuthUserHasAdminRole) {
			addRolesToUserEntity(userDetails, oldUserDetailsEntity)
		}
		return oldUserDetailsEntity.toUserDetails()
	}

	/**
	 * Обновление основных полей с сохранением ролей, изображений и т. д.
	 */
	@Transactional
	override fun updateFromExcel(userDetails: UserDetails): UserDetails {
		val oldUserDetailsEntity = userDetailsRepository.findByIdOrNull(userDetails.user.id) ?: run {
			throw UserNotFoundException()
		}
		with(oldUserDetailsEntity) {
			user.dept = userDetails.user.dept?.toDeptEntity()
			userDetails.user.authEmail?.let { user.authEmail = it }
			user.firstname = userDetails.user.firstname
			user.patronymic = userDetails.user.patronymic
			user.lastname = userDetails.user.lastname
			user.post = userDetails.user.post
			if (userDetails.user.gender != Gender.UNDEF) {
				user.gender = userDetails.user.gender
			}
			if (userDetails.user.roles != emptySet<RoleUser>()) {
				addRolesToUserEntity(userDetails, oldUserDetailsEntity)
			}
			schedule = userDetails.schedule
			phone = userDetails.phone
			tabId = userDetails.tabId
		}
		return oldUserDetailsEntity.toUserDetailsLazy()
	}

	private fun addRolesToUserEntity(
		userDetails: UserDetails, //new
		userDetailsEntity: UserDetailsEntity //old
	) {
		val newRoles = userDetails.user.roles.map { roleEnum ->
			RoleEntity(roleUser = roleEnum, user = userDetailsEntity.user)
		}

		with(userDetailsEntity.user.roles) {
			// this - oldRoles
			var delRole: RoleEntity? = null
			this.forEach { oldRole ->
				newRoles.find { newRole -> oldRole.roleUser == newRole.roleUser } ?: run {
					delRole = oldRole
				}
			}
			delRole?.let { this.remove(it) }

			var addRole: RoleEntity? = null
			newRoles.forEach { newRole ->
				this.find { oldRole -> oldRole.roleUser == newRole.roleUser } ?: run {
					addRole = newRole
				}
			}
			addRole?.let { this.add(it) }
		}
	}

	@Transactional
	override fun doesOwnerWithEmailExist(email: String): Boolean {
		val roles = roleRepository.findByRoleUserAndUserAuthEmail(
			roleUser = RoleUser.OWNER,
			userEmail = email
		)
		return roles.isNotEmpty()
	}

	@Transactional
	override fun findByAuthEmailWithDept(authEmail: String): List<User> {
		return userRepository.findByAuthEmailIgnoreCase(authEmail = authEmail).map {
			it.toUserWithDeptAndCompany()
		}
	}

	@Transactional
	override fun findByFullNameAndDeptsIds(
		fullName: FullName,
		deptsIds: List<Long>
	): UserDetails? {
		return userDetailsRepository.findByFullNameAndDeptsIds(
			firstname = fullName.firstName,
			lastName = fullName.lastName,
			patronymic = fullName.patronymic,
			deptsIds = deptsIds
		).firstOrNull()?.toUserDetailsWithRoles()
	}

	@Transactional
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

	@Transactional
	override fun findByDeptsExclude(
		deptId: Long,
		awardId: Long,
		actionType: ActionType?,
		baseQuery: BaseQuery
	): PageResult<User> {
		val pageRequest = baseQuery.toPageRequest()
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)

		val filter = baseQuery.filter.toSearchUpperOrNull()

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

	@Transactional
	override fun findByIdDetails(userId: Long): UserDetails? {
		return userDetailsRepository.findByUserId(userId)?.toUserDetails()
	}

	@Transactional
	override fun deleteById(userId: Long, deptId: Long?) {
		userRepository.deleteById(userId)
		deptId?.let { deptRepository.deleteById(it) } // Если Владелец, удаляем его отдел
	}

	@Transactional
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

	@Transactional
	override fun deleteImage(userId: Long, imageId: Long): BaseImage {
		val userImageEntity = userImageRepository.findByIdAndUserId(userId = userId, imageId = imageId) ?: run {
			throw ImageNotFoundException()
		}
		userImageRepository.delete(userImageEntity)
		return userImageEntity.toBaseImage()
	}

	@Transactional
	override fun setMainImage(userId: Long): BaseImage? {
		val userDetailsEntity = userDetailsRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
		val userEntity = userDetailsEntity.user

		val images = userDetailsEntity.images
		var userImageEntity = images.firstOrNull() ?: run {
			userEntity.mainImg = null
			userEntity.normImg = null
			return null
		}

		images.forEach {
			if (it.createdAt > userImageEntity.createdAt) {
				userImageEntity = it
			} else if (it.main) {
				it.main = false
			}
		}

		userImageEntity.main = true
		userEntity.mainImg = if (userImageEntity.miniUrl != null) userImageEntity.miniUrl else userImageEntity.normalUrl
		userEntity.normImg = userImageEntity.normalUrl
		return userImageEntity.toBaseImage()
	}

	@Transactional
	override fun updateAllUserImg() {
		val users = userRepository.findAll()
		users.forEach {
			val id = it.id ?: return@forEach
			val img = setMainImage(id)
			println(img)
		}
	}

	@Transactional
	override fun getGenderCountByDept(deptId: Long, subdepts: Boolean): GenderCount {
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = subdepts)
		return userRepository.genderCount(deptsIds = deptsIds)
	}

	@Transactional
	override fun getUsersWithActivity(deptId: Long, baseQuery: BaseQuery): List<UserAward> {
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)
		return userAwardRepository.findByDeptIdIn(deptsIds = deptsIds).map { it.toUserActivity() }
	}

	@Transactional
	override fun getUsersWithAward(deptId: Long, baseQuery: BaseQuery): List<UserAward> {
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)
		return userAwardRepository.findByDeptIdIn(deptsIds = deptsIds).map { it.toUserAward() }
	}

	@Transactional
	override fun getUsersWithAwardCount(deptId: Long, baseQuery: BaseQuery): PageResult<User> {
		val deptsIds = deptUtil.getDepts(deptId = deptId, subdepts = baseQuery.subdepts)
		val users = userRepository.findUsersWithAwardCount(
			deptsIds = deptsIds,
			filter = baseQuery.filter.toSearchUpperOrNull(),
			minDateNull = baseQuery.minDate == null,
			maxDateNull = baseQuery.maxDate == null,
			minDate = baseQuery.minDate,
			maxDate = baseQuery.maxDate,
			pageable = baseQuery.toPageRequest()
		)
		return users.toPageResult { it.toUser() }
	}

	@Transactional
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
	@Transactional
	override fun doesUserOwnerRole(userId: Long): Boolean {
		return roleRepository.countByUserIdAndRoleUser(
			userId = userId,
			roleUser = RoleUser.OWNER
		) > 0
	}

	@Transactional
	override fun activityByUserExist(userId: Long): Boolean {
		return activityRepository.countByUserIdAndActiv(userId = userId) > 0
	}

	@Transactional
	override fun moveUserToArchive(userId: Long) {
		userRepository.moveUserToArchive(userId = userId)
	}

	/**
	 * Проверка есть ли заданный email во всем дереве отделов
	 */
	@Transactional
	override fun validateEmail(deptId: Long, email: String): Boolean {
		val deptIds = deptUtil.getAllDeptIds(deptId)
		return userRepository.countByAuthEmailIgnoreCaseAndDeptIdIn(
			authEmail = email, deptsIds = deptIds
		) > 0
	}

	/**
	 * Проверка, существует ли сотрудник с email в отделе
	 */
	@Transactional
	override fun validateByDeptIdAndEmailExist(deptId: Long, email: String): Boolean {
		return userRepository.countByDeptIdAndAuthEmailIgnoreCase(
			authEmail = email, deptId = deptId
		) > 0
	}

	override fun getGenderByName(firstname: String, lastname: String): Gender {
		return userDetailsRepository.getGenderByName(firstname = firstname, lastname = lastname).toGender()
	}

}