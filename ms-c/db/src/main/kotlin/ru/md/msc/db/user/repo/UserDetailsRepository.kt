package ru.md.msc.db.user.repo

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msc.db.user.model.UserDetailsEntity

@Repository
interface UserDetailsRepository : JpaRepository<UserDetailsEntity, Long> {

	@EntityGraph("withUserDept")
	fun findByUserId(userId: Long): UserDetailsEntity?

	@EntityGraph("withUserDept")
	fun findByUserAuthEmail(authEmail: String): UserDetailsEntity?

//	@EntityGraph("withUser")
//	@Query("""
//		from UserDetailsEntity u where u.user.dept.id=:deptId and
//		u.user.authEmail=:authEmail
//	""")
//	fun findByDeptIdAndAuthEmail(deptId: Long, authEmail: String): UserDetailsEntity?

}
