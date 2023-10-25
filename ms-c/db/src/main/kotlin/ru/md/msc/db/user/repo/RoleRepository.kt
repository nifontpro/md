package ru.md.msc.db.user.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.base_db.user.model.RoleEntity
import ru.md.base_domain.user.model.RoleUser

@Repository
interface RoleRepository : JpaRepository<RoleEntity, Long> {

	fun findByRoleUserAndUserAuthEmail(roleUser: RoleUser, userEmail: String): List<RoleEntity>

	fun countByUserIdAndRoleUser(userId: Long, roleUser: RoleUser): Int
}