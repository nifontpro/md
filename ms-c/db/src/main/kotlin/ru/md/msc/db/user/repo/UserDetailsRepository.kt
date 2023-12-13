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
	fun findByFullNameAndDeptsIds(
		firstname: String,
		lastName: String,
		patronymic: String,
		deptsIds: List<Long>
	): List<UserDetailsEntity>

}
