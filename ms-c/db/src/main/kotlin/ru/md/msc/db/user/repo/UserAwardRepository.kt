package ru.md.msc.db.user.repo

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msc.db.user.model.UserAwardEntity

@Repository
interface UserAwardRepository : JpaRepository<UserAwardEntity, Long> {
	@EntityGraph("userAwardWithDept")
	fun findByDeptIdIn(deptsIds: List<Long>): List<UserAwardEntity>
}