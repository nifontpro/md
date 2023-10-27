package ru.md.base_db.pay.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.base_db.pay.model.UserPayEntity

@Repository
interface BaseUserPayRepo : JpaRepository<UserPayEntity, Long> {
	fun findByUserId(userId: Long): UserPayEntity?
}