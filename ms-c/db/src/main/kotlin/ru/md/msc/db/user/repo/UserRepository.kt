package ru.md.msc.db.user.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.user.model.UserEntity


@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {

	@EntityGraph("withDept")
	fun findByAuthEmailIgnoreCase(authEmail: String): List<UserEntity>

	fun findByDeptIdAndLastnameLikeIgnoreCase(deptId: Long, lastname: String, pageable: Pageable): Page<UserEntity>

	@EntityGraph("withDept")
	fun findByDeptIdInAndLastnameLikeIgnoreCase(
		deptsIds: List<Long>,
		lastname: String,
		pageable: Pageable
	): Page<UserEntity>

	@Modifying
	@Query("delete from UserEntity u where u.id = :userId")
	override fun deleteById(userId: Long)

	/**
	 * Найти id отдела сотрудника
	 */
	@Query("select u.dept.id from UserEntity u where u.id = :userId")
	fun finDeptId(userId: Long): Long?

}