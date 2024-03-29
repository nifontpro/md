package ru.md.msc.db.user.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.base_db.user.model.UserImageEntity

@Repository
interface UserImageRepository : JpaRepository<UserImageEntity, Long> {

	fun findAllByUserIdAndMain(userId: Long, main: Boolean): List<UserImageEntity>

	@Query("from UserImageEntity i where i.userId in :userIds and i.main = true")
	fun findMainImages(userIds: List<Long>): List<UserImageEntity>

	@Query("from UserImageEntity i where i.id = :imageId and i.userId = :userId")
	fun findByIdAndUserId(imageId: Long, userId: Long): UserImageEntity?

}