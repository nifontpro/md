package ru.md.msc.db.user.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.user.model.image.UserImageEntity

@Repository
interface UserImageRepository : JpaRepository<UserImageEntity, Long> {

	fun findAllByUserIdAndMain(userId: Long, main: Boolean): List<UserImageEntity>

	@Query("from UserImageEntity i where i.userId in :userIds and i.main = true")
	fun findMainImages(userIds: List<Long>): List<UserImageEntity>

}