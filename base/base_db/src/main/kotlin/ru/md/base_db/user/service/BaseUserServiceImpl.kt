package ru.md.base_db.user.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.md.base_db.user.model.mappers.toUserOnlyRoles
import ru.md.base_db.user.repo.BaseUserRepository
import ru.md.base_domain.user.biz.errors.UserNotFoundException
import ru.md.base_domain.user.model.User
import ru.md.base_domain.user.service.BaseUserService

@Service
class BaseUserServiceImpl(
	private val baseUserRepository: BaseUserRepository
): BaseUserService {

	@Transactional
	override fun findById(userId: Long): User? {
		return baseUserRepository.findByIdOrNull(userId)?.toUserOnlyRoles()
	}

	override fun findDeptIdByUserId(userId: Long): Long {
		return baseUserRepository.findDeptId(userId = userId) ?: throw UserNotFoundException()
	}

}