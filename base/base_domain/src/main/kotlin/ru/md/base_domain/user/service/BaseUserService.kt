package ru.md.base_domain.user.service

import ru.md.base_domain.user.model.User

interface BaseUserService {
	fun findById(userId: Long): User?
	fun findDeptIdByUserId(userId: Long): Long
}