package ru.md.msc.db.user.repo

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

	fun findByDeptId(deptId: Long): List<UserEntity>

//	@Modifying
//	@Query("delete from UserEntity u where u.id = :userId")
//	fun deleteUserById(userId: Long)

}