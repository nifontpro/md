package ru.md.base_db.user.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.base_db.user.model.UserEntity

@Repository
interface BaseUserRepository : JpaRepository<UserEntity, Long> {
	/**
	 * Найти id отдела сотрудника
	 */
	@Query("select u.dept.id from UserEntity u where u.id = :userId")
	fun findDeptId(userId: Long): Long?

}