package ru.md.msc.db.user.repo

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.user.model.UserDetailsEntity

@Repository
interface UserDetailsRepository : JpaRepository<UserDetailsEntity, Long> {
//
////	@EntityGraph("withUser")
//	fun getDetails(): List<UserDetailsEntity>
//
	@EntityGraph("withUserRoles")
	@Query("from UserDetailsEntity u")
	fun getsAll(): List<UserDetailsEntity>
}