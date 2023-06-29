package ru.md.msc.db.user.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.user.model.IUser
import ru.md.msc.db.user.model.UserEntity
import ru.md.msc.domain.user.model.GenderCount
import java.time.LocalDateTime


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
	fun findDeptId(userId: Long): Long?

	/**
	 * Найти parentId отдела сотрудника
	 */
	@Query("select u.dept.parentId from UserEntity u where u.id = :userId")
	fun findParentDeptId(userId: Long): Long

	/**
	 * Количество сотрудников по полам в отделах
	 */
	@Query(
		"""
			select new ru.md.msc.domain.user.model.GenderCount(
				(select count(*) from UserEntity u where u.dept.id in :deptsIds and u.gender = 'M'),
				(select count(*) from UserEntity u where u.dept.id in :deptsIds and u.gender = 'F'),
				(select count(*) from UserEntity u where u.dept.id in :deptsIds and u.gender is null)
			)
		"""
	)
	fun genderCount(deptsIds: List<Long>): GenderCount

	fun findByDeptIdIn(deptsIds: List<Long>): List<UserEntity>

	@Suppress("SqlRedundantCodeInCoalesce")
	@Query(
		"""
		select 
			u.id as userId,
			u.firstname,
			u.lastname,
			u.patronymic,
			u.post,
			d.id as deptId,
			d.name as deptName,
			d.classname,
			(select count (*) from md.activity i where i.user_id=u.id and i.is_activ and i.action_code='A' and
					(coalesce(:minDate, null) is null or i.date >= :minDate) and (coalesce(:maxDate, null) is null or i.date <= :maxDate)
			) as awardCount
			
		from users.user_data u left join dep.dept d on u.dept_id = d.id
		where u.dept_id in (:deptsIds)
	""",
		countQuery = """
			select count(*) from users.user_data u where u.dept_id in :deptsIds
		""", nativeQuery = true
	)
	fun findUsersWithAwardCount(
		deptsIds: List<Long>,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
		pageable: Pageable
	): Page<IUser>
}