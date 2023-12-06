package ru.md.msc.db.user.repo

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.user.model.UserDetailsEntity

@Repository
interface UserDetailsRepository : JpaRepository<UserDetailsEntity, Long> {

	@Query("select * from gender.get_gender(:firstname, :lastname)", nativeQuery = true)
	fun getGenderByName(firstname: String, lastname: String): String?

	@EntityGraph("withUserDept")
	fun findByUserId(userId: Long): UserDetailsEntity?

	@Query(
		"""
		from UserDetailsEntity u where 
		u.user.firstname like :firstname and
		u.user.lastname like :lastName and
		u.user.patronymic like :patronymic and
		u.user.dept.id in :deptsIds
	"""
	)
	fun findIdByFullNameAndDeptsIds(
		firstname: String,
		lastName: String,
		patronymic: String,
		deptsIds: List<Long>
	): List<UserDetailsEntity>

	@Query(
		"""
		from UserDetailsEntity d 
		where d.tabId=:tabId and d.user.dept.id=:deptId
	"""
	)
	fun findIdByTabIdAndDeptId(
		tabId: Long,
		deptId: Long
	): List<UserDetailsEntity>

//	@EntityGraph("withUser")
//	@Query("""
//		from UserDetailsEntity u where u.user.dept.id=:deptId and
//		u.user.authEmail=:authEmail
//	""")
//	fun findByDeptIdAndAuthEmail(deptId: Long, authEmail: String): UserDetailsEntity?

}
