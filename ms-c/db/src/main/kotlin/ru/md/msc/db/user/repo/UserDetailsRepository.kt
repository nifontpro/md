package ru.md.msc.db.user.repo

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.user.model.UserDetailsEntity

@Repository
interface UserDetailsRepository : JpaRepository<UserDetailsEntity, Long> {

	@EntityGraph("withUserDept")
	fun findByUserId(userId: Long): UserDetailsEntity?

	@Query(
		"""
		select d.user_id from users.user_details d 
		left join users.user_data u on d.user_id=u.id
		where d.tab_id=:tabId and u.dept_id=:deptId limit 1
	""", nativeQuery = true
	)
	fun findIdByTabIdAndDeptId(
		tabId: Long,
		deptId: Long
	): Long?

//	@EntityGraph("withUser")
//	@Query("""
//		from UserDetailsEntity u where u.user.dept.id=:deptId and
//		u.user.authEmail=:authEmail
//	""")
//	fun findByDeptIdAndAuthEmail(deptId: Long, authEmail: String): UserDetailsEntity?

}
