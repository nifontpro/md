package ru.md.msc.db.user.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msc.db.user.model.role.RoleEntity
import ru.md.msc.domain.user.model.RoleUser

@Repository
interface RoleRepository : JpaRepository<RoleEntity, Long> {

	fun findByRoleUserAndUserEmail(roleUser: RoleUser, userEmail: String): List<RoleEntity>

}