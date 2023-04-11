package ru.md.msc.db.user.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msc.db.user.model.role.RoleEntity
import ru.md.msc.domain.user.model.RoleEnum

@Repository
interface RoleRepository : JpaRepository<RoleEntity, Long> {

	fun findByRoleEnumAndUserEmail(roleEnum: RoleEnum, userEmail: String): List<RoleEntity>

}